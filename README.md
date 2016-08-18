# AutoNewLineLayout

自动换行Layout

- 处理了超长标签显示问题
- 添加了控制子控件之间的横向间隙和纵向间隙


#### 效果图

![shot1](https://github.com/LineChen/AutoNewLineLayout/blob/master/screenshots/shot1.png)

超长标签

![shot2](https://github.com/LineChen/AutoNewLineLayout/blob/master/screenshots/shot2.png)

#### 使用

布局

```java
    <com.beiing.autonewlinelayout.widgets.AutoNewLineLayout
        android:id="@+id/anl_tags"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="#90a4ae"
        android:padding="4dp"
        app:horizonalSpace="10dp"
        app:vertivalSpace="@dimen/vertical_space"
        />

```

```java

AutoNewLineLayout autoNewLineLayout;

    String[] tags = {"诛仙", "青云志" , "老九门", "花千骨", "琅琊榜", "伪装者", "仙剑奇侠传",
            "诛仙", "青云志" , "老九门", "花千骨", "琅琊榜", "伪装者", "仙剑奇侠传仙剑奇侠传仙剑奇侠传仙剑奇侠传仙剑奇侠传仙剑奇侠传"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoNewLineLayout = (AutoNewLineLayout) findViewById(R.id.anl_tags);

        addTags();
    }

    private void addTags() {
        for (int i = 0; i < tags.length; i++) {
            TextView tv = new TextView(this);
            tv.setBackgroundResource(R.drawable.radius_border_teal_solod_white);
            tv.setText(tags[i]);
            autoNewLineLayout.addView(tv);
        }
    }

```


















