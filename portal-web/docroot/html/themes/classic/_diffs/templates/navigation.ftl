<nav class="${nav_css_class}" id="navigation">
	<h1>
		<span><@liferay.language key="navigation" /></span>
	</h1>

	<ul>
		<#list nav_items as nav_item>
			<#assign navHasChildren = nav_item.hasChildren()>
			<#assign nav_item_class = "">

			<#if nav_item.isSelected()>
				<#assign nav_item_class = "selected">
			</#if>
			<#if navHasChildren>
				<#assign nav_item_class = nav_item_class + " has-children parent-menu">
			</#if>

			<li class="${nav_item_class}">
				<a href="${nav_item.getURL()}" ${nav_item.getTarget()}>
						<span>
							${nav_item.icon()} ${nav_item.getName()}
						</span>

						<#if navHasChildren>
							<span class="triangle">&#9660;</span>
						</#if>
					</a>

				<#if navHasChildren>
					<ul class="child-menu">
						<#list nav_item.getChildren() as nav_child>
							<#assign hasGrandChildren = nav_child.hasChildren()>
							<#assign nav_child_class = "">

							<#if nav_child.isSelected() || nav_child.isChildSelected()>
								<#assign nav_child_class = "selected">
							</#if>
							<#if hasGrandChildren>
								<#assign nav_child_class = nav_child_class + " has-children">
							</#if>

							<li class="${nav_child_class}">
								<a href="${nav_child.getURL()}" ${nav_child.getTarget()}><span>${nav_child.icon()} ${nav_child.getName()}</span>

									<#if hasGrandChildren>
										<span class="triangle child-triangle">&#9658;</span>
									</#if>
								</a>

								<#if hasGrandChildren>
									<ul class="grandchild-menu">
										<#list nav_child.getChildren() as nav_grandchild>
											<#if nav_grandchild.isSelected()>
												<li class="selected">
											<#else>
												<li>
											</#if>
												<a href="${nav_grandchild.getURL()}" ${nav_grandchild.getTarget()}>${nav_grandchild.getName()}</a>
											</li>
										</#list>
									</ul>
								</#if>
							</li>
						</#list>
					</ul>
				</#if>
			</li>
		</#list>
	</ul>
</nav>