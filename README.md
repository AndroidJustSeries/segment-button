# segment-button

It's a very basic segmented button.

![segmentview](https://user-images.githubusercontent.com/5418274/74506035-ffb5f100-4f3b-11ea-863a-508ff498e04e.gif)


Edit root/app/build.gradle like below.
```
allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
}
```

and:
```
dependencies {
    implementation 'com.github.AndroidJustSeries:segment-button:1.0.0'
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