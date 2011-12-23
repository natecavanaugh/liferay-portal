/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.scheduler.SchedulerEngine;
import com.liferay.portal.messaging.LayoutsLocalPublisherRequest;
import com.liferay.portal.messaging.LayoutsRemotePublisherRequest;
import com.liferay.portal.service.base.QuartzLocalServiceBaseImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class QuartzLocalServiceImpl extends QuartzLocalServiceBaseImpl {

	public void checkQuartzTables() throws SystemException {
		createQuartzTables();
		updateQuartzJobDetails();
		updateQuartzTriggers();
	}

	protected byte[] convertMessageToJSON(byte[] jobData)
		throws Exception {

		ObjectInputStream objectInputStream =
			new BackwardCompatibleObjectInputStream(
				new UnsyncByteArrayInputStream(jobData));

		Map<Object, Object> jobDataMap =
			(Map<Object, Object>)objectInputStream.readObject();

		objectInputStream.close();

		Map<Object, Object> tempJobDataMap = new HashMap<Object, Object>(
			jobDataMap);

		jobDataMap.clear();

		boolean modifiedKeys = false;

		for (Map.Entry<Object, Object> entry : tempJobDataMap.entrySet()) {
			Object key = entry.getKey();

			if (key instanceof String) {
				String stringKey = (String)key;

				if (stringKey.startsWith("destination")) {
					key = SchedulerEngine.DESTINATION_NAME;
				}
				else if (stringKey.equals("messageBody")) {
					key = SchedulerEngine.MESSAGE;

					jobDataMap.put(
						key, convertOldJsonStringToObject(entry.getValue()));

					continue;
				}
				else {
					key = stringKey.toUpperCase();
				}

				modifiedKeys = true;
			}

			jobDataMap.put(key, entry.getValue());
		}

		Object object = jobDataMap.get(SchedulerEngine.MESSAGE);

		if ((object == null) || (object instanceof String) || !modifiedKeys) {
			return null;
		}

		Message message = null;

		if (object instanceof Message) {
			message = (Message)object;
		}
		else {
			message = new Message();

			message.setPayload(object);
		}

		String messageJSON = JSONFactoryUtil.serialize(message);

		jobDataMap.put(SchedulerEngine.MESSAGE, messageJSON);

		UnsyncByteArrayOutputStream newJobDataOutputStream =
			new UnsyncByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
			newJobDataOutputStream);

		objectOutputStream.writeObject(jobDataMap);

		objectOutputStream.close();

		return newJobDataOutputStream.toByteArray();
	}

	@SuppressWarnings("deprecation")
	protected Object convertOldJsonStringToObject(Object object) {
		if (!(object instanceof String)) {
			return null;
		}

		String jsonString = (String)object;

		JSONObject jsonObject = null;

		try {
			jsonObject = JSONFactoryUtil.createJSONObject(jsonString);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		String oldJavaClass = jsonObject.getString("javaClass");

		if (oldJavaClass.equals(
				com.liferay.portlet.communities.messaging.
					LayoutsLocalPublisherRequest.class.getName())) {

			jsonObject.put(
				"javaClass", LayoutsLocalPublisherRequest.class.getName());
		}
		else if (oldJavaClass.equals(
					com.liferay.portlet.communities.messaging.
						LayoutsRemotePublisherRequest.class.getName())) {

			jsonObject.put(
				"javaClass", LayoutsRemotePublisherRequest.class.getName());
		}

		jsonString = jsonObject.toString();

		return JSONFactoryUtil.deserialize(jsonString);
	}

	protected byte[] convertPropertiesToMap(byte[] jobData) throws Exception {
		InputStream inputStream = null;

		try {
			inputStream = new ByteArrayInputStream(jobData);

			new ObjectInputStream(inputStream);

			return jobData;
		}
		catch (Exception e) {
			Properties properties = new Properties();

			properties.load(inputStream);

			ByteArrayOutputStream byteArrayOutputStream =
				new ByteArrayOutputStream();

			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);

			objectOutputStream.writeObject(
				new HashMap<Object, Object>(properties));

			objectOutputStream.flush();

			return byteArrayOutputStream.toByteArray();
		}
		finally {
			inputStream.close();
		}
	}

	protected void createQuartzTables() throws SystemException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getConnection();

			ps = con.prepareStatement(
				"select count(*) from QUARTZ_JOB_DETAILS");

			rs = ps.executeQuery();

			if (rs.next()) {
				return;
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}

		DB db = DBFactoryUtil.getDB();

		try {
			db.runSQLTemplate("quartz-tables.sql", false);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	protected void updateQuartzJobDetails() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<Object[]> arrays = new ArrayList<Object[]>();

		try {
			con = DataAccess.getConnection();

			ps = con.prepareStatement(
				"select JOB_NAME, JOB_GROUP, JOB_DATA from QUARTZ_JOB_DETAILS");

			rs = ps.executeQuery();

			while (rs.next()) {
				String jobName = rs.getString("JOB_NAME");
				String jobGroup = rs.getString("JOB_GROUP");
				byte[] jobData = rs.getBytes("JOB_DATA");

				jobData = convertPropertiesToMap(jobData);
				jobData = convertMessageToJSON(jobData);

				if (jobData == null) {
					continue;
				}

				Object[] array = new Object[3];

				array[0] = jobName;
				array[1] = jobGroup;
				array[2] = jobData;

				arrays.add(array);
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}

		if (arrays.isEmpty()) {
			return;
		}

		try {
			con = DataAccess.getConnection();

			ps = con.prepareStatement(
				"update QUARTZ_JOB_DETAILS set JOB_DATA = ? where JOB_NAME = " +
					"? and JOB_GROUP = ?");

			for (Object[] array : arrays) {
				String jobName = (String)array[0];
				String jobGroup = (String)array[1];
				byte[] jobData = (byte[])array[2];

				ps.setBytes(1, jobData);
				ps.setString(2, jobName);
				ps.setString(3, jobGroup);

				ps.executeUpdate();
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	protected void updateQuartzTriggers() {
		DB db = DBFactoryUtil.getDB();

		String dbType = db.getType();

		if (!dbType.equals(DB.TYPE_SYBASE)) {
			return;
		}

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<Object[]> arrays = new ArrayList<Object[]>();

		try {
			con = DataAccess.getConnection();

			ps = con.prepareStatement(
				"select JOB_NAME, JOB_GROUP, JOB_DATA from QUARTZ_TRIGGERS");

			rs = ps.executeQuery();

			while (rs.next()) {
				String jobName = rs.getString("JOB_NAME");
				String jobGroup = rs.getString("JOB_GROUP");
				byte[] jobData = rs.getBytes("JOB_DATA");

				if ((jobData == null) || (jobData.length != 1)) {
					continue;
				}

				if (jobData[0] != 0) {
					continue;
				}

				Object[] array = new Object[2];

				array[0] = jobName;
				array[1] = jobGroup;

				arrays.add(array);
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}

		if (arrays.isEmpty()) {
			return;
		}

		try {
			con = DataAccess.getConnection();

			ps = con.prepareStatement(
				"update QUARTZ_TRIGGERS set JOB_DATA = ? where JOB_NAME = ? " +
					"and JOB_GROUP = ?");

			for (Object[] array : arrays) {
				String jobName = (String)array[0];
				String jobGroup = (String)array[1];

				ps.setBytes(1, null);
				ps.setString(2, jobName);
				ps.setString(3, jobGroup);

				ps.executeUpdate();
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		QuartzLocalServiceImpl.class);

	private class BackwardCompatibleObjectInputStream
		extends ObjectInputStream {

		public BackwardCompatibleObjectInputStream(InputStream inputStream)
			throws IOException {

			super(inputStream);
		}

		@Override
		@SuppressWarnings("deprecation")
		protected ObjectStreamClass readClassDescriptor()
			throws ClassNotFoundException, IOException {

			ObjectStreamClass objectStreamClass = super.readClassDescriptor();

			String name = objectStreamClass.getName();

			if (name.equals(Message.class.getName())) {
				return ObjectStreamClass.lookup(Message.class);
			}
			else if (name.equals(
						com.liferay.portlet.communities.messaging.
							LayoutsLocalPublisherRequest.class.getName())) {

				return ObjectStreamClass.lookup(
					LayoutsLocalPublisherRequest.class);
			}
			else if (name.equals(
						com.liferay.portlet.communities.messaging.
							LayoutsRemotePublisherRequest.class.getName())) {

				return ObjectStreamClass.lookup(
					LayoutsRemotePublisherRequest.class);
			}
			else {
				return objectStreamClass;
			}
		}

	}

}