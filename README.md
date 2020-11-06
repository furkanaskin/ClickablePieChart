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
	implementation 'com.github.furkanaskin:ClickablePieChart:1.0.7'
}
```

## Usage

```kotlin
        val pieChart = PieChart(
            slices = provideSlices(), clickListener = null, sliceStartPoint = 0f, sliceWidth = 80f
        ).build()

        chart.setPieChart(pieChart)
```
Also you can use **Kotlin DSL** for building your chart.
```kotlin
        val pieChartDSL = buildChart {
            slices { provideSlices() }
            sliceWidth { 80f }
            sliceStartPoint { 0f }
            clickListener { angle, index ->
                // ...
            }
        }
        chart.setPieChart(pieChartDSL)
```
To setup with legend you need an root layout for legend.
```kotlin
chart.showLegend(legendLayout)
```
Or use with custom legend adapter by inheriting from LegendAdapter
```kotlin
chart.showLegend(legendLayout, CustomLegendAdapter())
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
  <tr>
    <td>app:showPercentage</td>
    <td>boolean</td>
    <td>Show percentage of slice beside popupText.</td>
  </tr>
  <tr>
    <td>app:animationDuration</td>
    <td>integer</td>
    <td>Animation duration with milliseconds.</td>
  </tr>
</tbody>
</table>
