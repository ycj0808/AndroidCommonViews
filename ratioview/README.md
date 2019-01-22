## 自定义RatioImageView

RatioImageView在宽度一定，通过设置ratio(高/宽)，来设置ImageView高度，从而达到图片按
长宽比例展示，从而达到保持原始的长宽比例。

使用方法:
```
    <me.icefire.ratio.view.RatioImageView
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        app:viewRatio="0.75"
        />
```

## 自定义 RatioRelativeLayout

RatioRelativeLayout继承自RelativeLayout，通过设置ratio，从而达到宽度一定，按比例设置高度

使用方法：
```
    <me.icefire.ratio.view.RatioRelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:viewRatio="0.5"
        >
```


引用：
```
    implementation 'me.icefire:ratioview:1.0.2'
```