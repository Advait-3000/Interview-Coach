# Window and Responsive Layout

This is a desktop app, but it must behave sensibly when resized.

- Target usable window: 1280×800; minimum supported target: 900×600.
- Use JavaFX layout panes that resize instead of absolute pixel positioning.
- Keep important actions visible at minimum size; allow the question content area to scroll.
- On narrower windows, stack dashboard cards and keep forms in one column.
- Tables should avoid clipped action controls; provide horizontal scrolling only when necessary.
- Do not assume a 4K screen or fixed OS display scaling.
- Test at 100%, 125%, and 150% display scaling where practical.
