AUI.add(
	'liferay-sortable',
	function(A) {
		var Sortable = A.Sortable;

		var STR_CONT = 'container';
		var STR_ID = 'id';
		var STR_NODES = 'nodes';

		A.mix(
			Sortable.prototype,
			{
				_x: null,

				initializer: function() {
					var instance = this;

					var del;

					var id = 'sortable-' + A.guid();

					var delConfig = {
						container: instance.get(STR_CONT),
						dragConfig: {
							groups: [ id ]
						},
						invalid: instance.get('invalid'),
						nodes: instance.get(STR_NODES),
						target: true
					};

					if (instance.get('handles')) {
						delConfig.handles = instance.get('handles');
					}

					del = new A.DD.Delegate(delConfig);

					instance.set(STR_ID, id);

					del.dd.plug(
						A.Plugin.DDProxy,
						{
							cloneNode: true,
							moveOnEnd: false
						}
					);

					instance.drop =  new A.DD.Drop(
						{
							bubbleTarget: del,
							groups: del.dd.get('groups'),
							node: instance.get(STR_CONT)
						}
					);

					instance.drop.on('drop:enter', A.bind(instance._onDropEnter, instance));

					instance._setDragMethod();

					del.on(
						{
							'drag:drag': A.bind(instance._dragMethod, instance),
							'drag:end': A.bind(instance._onDragEnd, instance),
							'drag:over': A.bind(instance._onDragOver, instance),
							'drag:start': A.bind(instance._onDragStart, instance)
						}
					);

					instance.delegate = del;

					Sortable.reg(instance, id);
				},

				_setDragMethod: function() {
					var instance = this;

					var dragMethod;

					var node = instance.get(STR_CONT).one(instance.get(STR_NODES));

					var floated = node ? node.getStyle('float') : 'none';

					if (floated === 'left' || floated === 'right') {
						dragMethod = instance._onDragHorizontal;
					}
					else {
						dragMethod = instance._onDrag;
					}

					instance._dragMethod = dragMethod;
				},

				_onDragHorizontal: function(e) {
					var instance = this;

					if (e.pageX < instance._x) {
						instance._up = true;
					}
					else if (e.pageX > instance._x) {
						instance._up = false;
					}

					instance._x = e.pageX;
				}
			},
			true
		);
	},
	'',
	{
		requires: ['sortable']
	}
);