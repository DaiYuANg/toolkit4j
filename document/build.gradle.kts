plugins {
  idea
  alias(libs.plugins.mkdocs)
}

mkdocs {
  sourcesDir = "src"
  strict = true
}