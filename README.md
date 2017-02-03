[![Build Status](https://travis-ci.org/wayfair/brickkit-android.svg)](https://travis-ci.org/wayfair/brickkit-android)
[![codecov.io](http://codecov.io/github/wayfair/brickkit-android/coverage.svg)](http://codecov.io/github/wayfair/brickkit-android)

<div align="center">
<span><img src="Docs/SampleImage/BrickKit.png" alt="BrickKit" title="BrickKit" ></span>
<span style="font-size:60px; writing-mode: tb-rl;">BrickKit</span>
</div>


## What is the BrickKit

BrickKit is a tool developed with the Android RecyclerView and GridLayout. With BrickKit, you can manage complex and different layouts (bricks) on the same page by one RecyclerView and DataManger. It's easy to reuse and extend bricks which highly reduces the code redundancy and difficulty of UI testing.


## How to import BrickKit as a library

Add new maven endpoint to your repositories

```
  maven {
    url  "http://wayfair.bintray.com/brickkit-android"
  }
```
And add it as a Gradle compine dependency
```
  compile "com.wayfair:brickkit-android:0.9.1"
```

## How to run BrickKit demo project

```
1. git clone 'XXXXXXX'
2. Open Android Studio -> New -> Import Project
3. Run BrickKit
```

## Features of BrickKit

### BasicFragment

![SimpleFragment](Docs/SampleImage/SimpleFragment.png)
```java
public class SimpleBrickFragment extends BrickFragment {
      private static final int HALF = 120;  //maxSpans == 240
      private int numberOfBricks = 20;

      public static SimpleBrickFragment newInstance(int numberOfBricks) {
          SimpleBrickFragment fragment = new SimpleBrickFragment();
          fragment.numberOfBricks = numberOfBricks;
          return fragment;
      }

      @Override
      public void createBricks() {
          for (int i = 0; i < numberOfBricks; i++) {
              TextBrick textBrick = new TextBrick(
                      getContext(),
                      new OrientationBrickSize(maxSpans()) {
                          @Override
                          protected int portrait() {
                              return dataManager.getMaxSpanCount();
                          }

                          @Override
                          protected int landscape() {
                              return HALF;
                          }
                      },
                      new InnerOuterBrickPadding() {
                          @Override
                          protected int innerPadding() {
                              return 5;
                          }

                          @Override
                          protected int outerPadding() {
                              return 10;
                          }
                      },
                      "Brick: " + i
              );
              dataManager.addLast(textBrick);
          }
    }

    public void addBehaviors() { }

    public int orientation() {
        return OrientationHelper.VERTICAL;
    }

    public boolean reverse() {
        return false;
    }
}

```

| Method   |      Description  |
|----------|:-------------:|
| createBricks |  Allows to constitute the different types of 'bricks' to the View.
| addBehaviors |  Adds complex layout interactions to the View.  
| orientation |   Defines the orientations in which the 'bricks' are layed out (GridLayoutManager.VERTICAL/GridLayoutManager.HORIZONTAL)
| reverse |  If false, the bricks will be added from the top of screen, otherwise it will be added from the bottom.


### Bricks with different spans

![Span Example](Docs/SampleImage/SpanExample.png)

```java
@Override
public void createBricks() {
    for (int i = 0; i < dataManager.getMaxSpanCount() / HALF; i++) {   // HALF == 120
        TextBrick textBrick = new TextBrick(
                getContext(),
                new OrientationBrickSize(maxSpans()) {
                    @Override
                    protected int portrait() {
                        return HALF;
                    }

                    @Override
                    protected int landscape() {
                        return HALF;
                    }
                },
                new InnerOuterBrickPadding() {
                    @Override
                    protected int innerPadding() {
                        return 5;
                    }

                    @Override
                    protected int outerPadding() {
                        return 10;
                    }
                },
                "Brick: " + i
        );
        dataManager.addLast(textBrick);
    }

    for (int i = 0; i < dataManager.getMaxSpanCount() / ONE_THIRD; i++) { //ONE_THIRD == 80
        TextBrick textBrick = new TextBrick(
                getContext(),
                new OrientationBrickSize(maxSpans()) {
                    @Override
                    protected int portrait() {
                        return ONE_THIRD;
                    }

                    @Override
                    protected int landscape() {
                        return HALF;
                    }
                },
                new InnerOuterBrickPadding() {
                    @Override
                    protected int innerPadding() {
                        return 5;
                    }

                    @Override
                    protected int outerPadding() {
                        return 10;
                    }
                },
                "Brick: " + i
        );
        dataManager.addLast(textBrick);
    }

    for (int i = 0; i < dataManager.getMaxSpanCount() / QUARTER; i++) { //QUARTER == 60
        TextBrick textBrick = new TextBrick(
                getContext(),
                new OrientationBrickSize(maxSpans()) {
                    @Override
                    protected int portrait() {
                        return QUARTER;
                    }

                    @Override
                    protected int landscape() {
                        return HALF;
                    }
                },
                new InnerOuterBrickPadding() {
                    @Override
                    protected int innerPadding() {
                        return 5;
                    }

                    @Override
                    protected int outerPadding() {
                        return 10;
                    }
                },
                "Brick: " + i
        );
        dataManager.addLast(textBrick);
    }
}
```

### Bricks with different behaviors

#### StickyHeader layout
![StickyHeader Example](Docs/SampleImage/StickyHeader.png)

```java
@Override
public void addBehaviors() {
     dataManager.addBehavior(new StickyHeaderBehavior(dataManager));
}

@Override
public void createBricks() {
        BaseBrick brick = new TextBrick(
                getContext(),
                new SimpleBrickSize(maxSpans()) {
                    @Override
                    protected int size() {
                        return dataManager.getMaxSpanCount();
                    }
                },
                "simple" + i
        );
        brick.setHeader(true);
        dataManager.addLast(brick);
    }
}
```

#### StickyFooter layout
![StickyFooter Example](Docs/SampleImage/StickyFooter.png)

```java
@Override
public void addBehaviors() {
     dataManager.addBehavior(new StickyFooterBehavior(dataManager));
}

@Override
public void createBricks() {
        BaseBrick brick = new TextBrick(
                getContext(),
                new SimpleBrickSize(maxSpans()) {
                    @Override
                    protected int size() {
                        return MAX_SPANS;
                    }
                },
                "simple" + i
        );
        brick.setFooter(true);
        dataManager.addLast(brick);
    }
}
```

#### Reverse layout
![Reverse Example](Docs/SampleImage/Reverse.png)
```java
@Override
public boolean reverse() {
    return true;
}
```

#### InfiniteScroll layout
```java
@Override
public void createBricks() {
    dataManager.getBrickRecyclerAdapter().setOnReachedItemAtPosition(
            new OnReachedItemAtPosition() {
                @Override
                public void bindingItemAtPosition(int position) {
                    if (position == dataManager.getRecyclerViewItems().size() - 1) {
                        page++;
                        addNewBricks();
                    }
                }
            }
    );

    addNewBricks();
}
```


#### Section layout
```java
public class FragmentBrickFragment extends BrickFragment {
    @Override
    public void createBricks() {
        for (int i = 0; i < 2; i++) {
            BaseBrick brick = new FragmentBrick(
                    getContext(),
                    new SimpleBrickSize(maxSpans()) {
                        @Override
                        protected int size() {
                            return dataManager.getMaxSpanCount() / 2;
                        }
                    },
                    getChildFragmentManager(),
                    SimpleBrickFragment.newInstance(50 * (i + 1)),
                    "simple" + i
            );
            dataManager.addLast(brick);
        }
    }
}
```
> FragmentBrick enables us to nest the brickFragment within the another one in which we could brick the simple bricks in the nested brickFragment and arrange the outer brickFragment as well.


## Customize your own brick
```java
public class CustomizedBrick extends BaseBrick {
  /**
   * Constructor.
   *
   * @param context context this brick exists in
   * @param spanSize size information for this brick
   * @param padding padding for this brick
   */
    public CustomizedBrick(Context context, BrickSize spanSize, BrickPadding padding) {
        super(context, spanSize, padding);
    }

    /**
     * Called by the BrickRecyclerAdapter to display the information in this brick to the specified ViewHolder.
     *
     * @param holder ViewHolder which should be updated.
     */    
    @Override
    public void onBindData(RecyclerView.ViewHolder holder) {

    }

    /**
     * Gets the template string for this brick type.
     *
     * @return the template string for this brick type
     */
    @Override
    public String getTemplate() {
        return null;
    }

    /**
     * Get layout resource id for this brick.
     *
     * @return the layout resource id for this brick
     */
    @Override
    public int getLayout() {
        return 0;
    }

    /**
     * Creates an instance of the {@link BrickViewHolder} for this class.
     *
     * @param itemView view to pass into the {@link BrickViewHolder}
     * @return the {@link BrickViewHolder}
     */    @Override
    public BrickViewHolder createViewHolder(View itemView) {
        return null;
    }
}
```

>- Configure layout xml file, template's name and bind the child view by your own.
>- Set customized innerPadding/outerPadding, portrait/landscape spanSize by extending BrickSize and BrickPadding.
>- Define different brick behaviors satisfying your needs.


## Manager your bricks with BrickDataManager

The 'BrickDataManager' manages the RecyclerView's adapter and manipulates the bricks. You could add/remove 'bricks' at different positions, get/replace 'bricks' at certain positions.

| Methods Used Frequently   |      Description  |
|----------|:-------------:|
| getRecyclerViewItems |  Gets all 'visible' bricks in the 'BaseDataManager'.
| getDataManagerItems |  Gets all bricks in the 'BaseDataManager'.
| addLast |  Inserts brick/Collection of bricks after all other bricks.
| addFirst |   Inserts brick/Collection of bricks before all other bricks.
| removeItem |  Remove a brick from the 'BaseDataManager'.
| brickWithLayout |  Retrieves a brick who's associated layout resource ID matches.
| brickAtPosition |  Retrieves a brick at a specific position.

## Credits

BrickKit is owned and maintained by [Wayfair](https://www.wayfair.com).

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).


## License

BrickKit is released under the Apache license. See LICENSE for details.
