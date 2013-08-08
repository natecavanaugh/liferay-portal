<nav class="${nav_css_class}" id="navigation" role="navigation">
	<ul role="menubar">
		<#list nav_items as nav_item>
			<#if nav_item.isSelected()>
				<li aria-selected="true" class="selected" id="layout_${nav_item.getLayoutId()}" role="presentation">
			<#else>
				<li id="layout_${nav_item.getLayoutId()}" role="presentation">
			</#if>

				<a href="${nav_item.getURL()}" ${nav_item.getTarget()} role="menuitem"><span>${nav_item.icon()} ${nav_item.getName()}</span></a>

				<#if nav_item.hasChildren()>
					<a aria-haspopup="true" href="${nav_item.getURL()}" ${nav_item.getTarget()} role="menuitem"><span>${nav_item.icon()} ${nav_item.getName()}</span></a>

					<ul class="child-menu" role="menu">
						<#list nav_item.getChildren() as nav_child>
							<#if nav_child.isSelected()>
								<li aria-selected="true" class="selected" id="layout_${nav_child.getLayoutId()}" role="presentation">
							<#else>
								<li id="layout_${nav_child.getLayoutId()}" role="presentation">
							</#if>

								<a href="${nav_child.getURL()}" ${nav_child.getTarget()} role="menuitem">${nav_child.getName()}</a>
							</li>
						</#list>
					</ul>
				<#else>
					<a aria-haspopup="false" href="${nav_item.getURL()}" ${nav_item.getTarget()} role="menuitem"><span>${nav_item.icon()} ${nav_item.getName()}</span></a>
				</#if>
			</li>
		</#list>
	</ul>
</nav>