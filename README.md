[![](https://jitpack.io/v/furkanaskin/ClickablePieChart.svg)](https://jitpack.io/#furkanaskin/ClickablePieChart)

# ClickablePieChart
Android Pie Chart library, supported with **Kotlin DSL**.

<img height="500" src="https://user-images.githubusercontent.com/22769589/93264550-f467c400-f7af-11ea-8d76-78fb0163fd04.jpg" alt="PieChart"/>

## Installation
Step 1. Add the JitPack repository to your build file
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency
```gradle
dependencies {
  implementation 'com.github.furkanaskin:ClickablePieChart:1.0.5'
}
```

## Usage

Use buildChart function for building your chart. That's it.
```kotlin
        val pieChart = buildChart {
            slices {
                arrayListOf(
                    Slice(
                        300f,
                        R.color.colorPrimary,
                        "Google"
                    ),
                    Slice(
                        1500f,
                        R.color.colorPrimaryDark,
                        "Facebook"
                    ),
                    Slice(
                        1240f,
                        R.color.materialIndigo600,
                        "Twitter"
                    ),
                    Slice(
                        700f,
                        R.color.colorAccent,
                        "Other"
                    )
                )
            }
            sliceWidth { 80f }
            sliceStartPoint { 0f }
            clickListener { angle, index -> }
        }

        chart.setPieChart(pieChart)
```
To setup with legend (Works with **RelativeLayout**)
```kotlin
chart.showLegend(legendLayout)
```
## XML Attributes
<table>
<thead>
  <tr>
    <th>XML Attribute</th>
    <th>Format</th>
    <th>Description</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td>app:popupText</td>
    <td>string</td>
    <td>Shows text after the slice data value in popup.</td>
  </tr>
  <tr>
    <td>app:centerColor</td>
    <td>color</td>
    <td>Center color of pie chart.</td>
  </tr>
  <tr>
    <td>app:showPopup</td>
    <td>boolean</td>
    <td>Show popup when user clicks on pie chart.</td>
  </tr>
</tbody>
</table>
