var fs = require('fs');

var args = {
	alloyFolder: null,
	outputFile: null
};

var A;

var modulesDependencies = {};

var moduleInfo = {};

function main() {
	resolveArguments();
	
	var YUI = require(args.alloyFolder + '/yui/yui.js').YUI;
	
	var AUI_config = require(args.alloyFolder + '/aui-base/aui-base-meta.js').AUI_config;
	
	A = YUI(
		{
			base: args.alloyFolder,
			combine: false
		}
	);
		
	var loader = A.Env._loader;
	
	var groups = AUI_config.groups;

	for (var group in groups) {
		loader.addGroup(groups[group], group);
	}

	loader.ignoreRegistered = true;
	
	loader.conditions = {};
	
	debugger;
	
	moduleInfo = loader.moduleInfo;

	/*
	loader.require(['aui-dialog-iframe', 'aui-text']);
	
	var res = loader.resolve(true);

	console.log(res);
	*/
	
	extractModulesDependencies();
	
	exportModuleDependencies(moduleInfo);
	
}

function exportModuleDependencies(struct) {
	var strigifiedOptions = JSON.stringify(struct);
	
	var writeFile = fs.writeFile || fs.write;
	
	writeFile(args.outputFile, strigifiedOptions);
}

function extractModuleDependencies(moduleName) {
	var loader = A.Env._loader;
	
	loader.required = {};
	
	loader.require([moduleName]);

	loader.calculate();
}

function extractModulesDependencies() {
	for (var moduleName in moduleInfo) {
		extractModuleDependencies(moduleName);
	}
}

function resolveArguments() {
	var params;
	
	if (typeof system != 'undefined') {
		params = system.args.slice(1);
	}
	else {
		params = process.argv.slice(2);
	}
	
	if (params[0]) {
		args.alloyFolder = params[0];
	}
	
	if (params[1]) {
		args.outputFile = params[1];
	}
}

if (require.main === module) {
	main();
}