# segment-button

It's a very basic segmented button.

![segmentview1](https://user-images.githubusercontent.com/5418274/74515360-c4262180-4f51-11ea-90f2-23faf181f624.gif)


Edit root/app/build.gradle like below.
```
allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
}
```

and: ![Version][![](https://jitpack.io/v/AndroidJustSeries/segment-button.svg)](https://jitpack.io/#AndroidJustSeries/segment-button)
```
dependencies {
    implementation 'com.github.AndroidJustSeries:segment-button:1.1.0'
}
```
## How to use
#XML
```
    <com.kds.just.segmentview.SegmentedView
        android:id="@+id/segment"
        android:layout_width="200dp"
        android:layout_height="80dp"
        android:layout_marginLeft="27dp"
        android:layout_marginTop="30dp"
        app:BgColorNormal="#ffffff"
        app:BgColorSelected="#727af2"
        app:BgRadius="3dp"
        app:BgStrokeColorNormal="#d8d8d8"
        app:BgStrokeColorSelected="#ff0000"
        app:textColorNormal="#d8d8d8"
        app:textColorSelected="#ffffff"
        app:textSize="13sp"
        app:StrokeWidth="3dp"/>
```
#JAVA
```
        SegmentedView segment = findViewById(R.id.segment);
        segment.addItem("item 0");
        segment.addItem("item 1");
        segment.addItem("item 2");
        segment.addItem("item 3");
```
