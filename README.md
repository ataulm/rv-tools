rv-tools
=============
custom item decorations and layout managers for RecyclerView.

Currently contains `SpacingItemDecoration` which matches the behaviour of GridView's `horizontalSpacing` and
`verticalSpacing` attributes - spacing _between_ items, not just margin around _every_ item. It's currently only working
for vertically scrolling lists, and only with LayoutManagers that support horizontal spans (GridLayoutManager).
