[![](https://jitpack.io/v/gsotti/ClickablePieChart.svg)](https://jitpack.io/#gsotti/ClickablePieChart)

# ClickablePieChart
Android Chart library, supported with **Kotlin DSL**.

<img height="500" src="/assets/device-2020-11-12-104411.png" alt="PieChart"/>

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
	implementation 'com.github.gsotti:ClickablePieChart:1.0.13'
}
```

## Usage

```kotlin
        val pieChart = PieChart(
            slices = provideSlices(), clickListener = null, sliceStartPoint = 0f, sliceWidth = 80f
        ).build()

        chart.setPieChart(pieChart)
```

Or create a BarChart

```kotlin
        val barChart = BarChart(
            slices = provideSlices(), clickListener = null
        ).build()

        chart.setBarChart(barChart)
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

Or create a BarChart

```kotlin
         val barChartDSL = buildBarChart {
                    slices { provideSlices() }
                    clickListener { percentage, index ->
                        // ...
                    }
                }
        chart.setBarChart(barChartDSL)
```


To setup with legend you need an root layout for legend.
```kotlin
chart.showLegend(legendLayout)
```
Or use with custom legend adapter by inheriting from LegendAdapter
```kotlin
chart.showLegend(legendLayout, CustomLegendAdapter())
```

Or if you use a barChart you can also change the orientation of the legendAdapter
```kotlin
 chart4.showLegend(rootLayout = legendLayout, orientation = LinearLayoutManager.HORIZONTAL or LinearLayoutManager.VERTICAL)
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
   <tr>
      <td>app:orientation</td>
      <td>string</td>
      <td>Orientation of BarChart (horizontal/vertical)</td>
    </tr>
</tbody>
</table>
