package com.grant.RxjavaDome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity：Rxjava：";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建create 被观察者的Observable对象
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        });

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始subscribe连接");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "对Next事件" + value + "作出响应");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        };

        observable.subscribe(observer);


        //    Disposable.dispose() 切断观察者 与 被观察者 之间的连接
        Observer<Integer> observer1 = new Observer<Integer>() {

            private Disposable mdisposable;

            @Override
            public void onSubscribe(Disposable disposable) {
                mdisposable = disposable;
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "对Next1事件" + value + "作出响应");
                if (value == 2) {
                    // 设置在接收到第二个事件后切断观察者和被观察者的连接
                    mdisposable.dispose();
                    Log.d(TAG, "已经切断了连接1：" + mdisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error1事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete1事件作出响应");
            }
        };

        observable.subscribe(observer1);


        //just()操作符的应用
        Observable.just("a", "b", "c", "d", "e")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "just开始subscribe  连接");
                    }

                    @Override
                    public void onNext(String value) {
                        Log.d(TAG, " just对Next1事件" + value + "作出响应");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "just对Error1事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "just对Complete1事件作出响应");
                    }
                });

        //fromArray（）操作符的应用
        Integer[] items = {0, 1, 2, 3, 4};
        Observable.fromArray(items)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "fromArray开始subscribe  连接");
                        Log.d(TAG, "数组遍历");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, " fromArray对Next1事件" + value + "作出响应");
                        Log.d(TAG, "数组中的元素 = " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "fromArray对Error1事件作出响应");
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "fromArray对Complete1事件作出响应");
                        Log.d(TAG, "遍历结束");
                    }
                });

        //fromIterable（）操作符的应用
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        Observable.fromIterable(list)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "fromIterable数组遍历");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "fromIterable数组中的元素 = " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "fromIterable对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "fromIterable遍历结束");
                    }
                });

// 该方法创建的被观察者对象发送事件的特点：仅发送Complete事件，直接通知完成
// Observable observable1=Observable.empty();
// 即观察者接收后会直接调用onCompleted（）
// <-- error() -->
// 该方法创建的被观察者对象发送事件的特点：仅发送Error事件，直接通知异常
// 可自定义异常
// Observable observable2=Observable.error(new RuntimeException())
// 即观察者接收后会直接调用onError（）
// <-- never() -->
// 该方法创建的被观察者对象发送事件的特点：不发送任何事件
// Observable observable3=Observable.never();
// 即观察者接收后什么都不调用

        //defer（） 延迟创建

        final Integer i = 10;
        Observable<Integer> observable1 = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(i);
            }
        });
        observable1.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "defer（）开始subscribe连接");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "defer（）对Next1事件" + value + "作出响应");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "defer（）对Error1事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "defer（）对Complete1事件作出响应");
            }
        });

        // timer() 该例子 = 延迟3s后，发送一个long类型数值
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始subscribe连接");
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });

        //interval()操作符
        // 参数1 = 第1次延迟时间；
        // 参数2 = 间隔时间数字；
        // 参数3 = 时间单位；
        Observable.interval(3, 1, TimeUnit.SECONDS)
                // 该例子发送的事件序列特点：延迟3s后发送事件，每隔1秒产生1个数字（从0开始递增1，无限个）
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "interval开始采用subscribe连接");
                    }

                    // 默认最先调用复写的onSubscribe（）
                    @Override
                    public void onNext(Long value) {
                        Log.d(TAG, "interval接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "interval对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "interval对Complete事件作出响应");
                    }
                });

        //intervalRange()操作符
        // 参数1 = 事件序列起始点；
        // 参数2 = 事件数量；
        // 参数3 = 第1次事件延迟发送时间；
        // 参数4 = 间隔时间数字；
        // 参数5 = 时间单位
        Observable.intervalRange(3, 10, 2, 1, TimeUnit.SECONDS)
                // 该例子发送的事件序列特点：
                // 1. 从3开始，一共发送10个事件；
                // 2. 第1次延迟2s发送，之后每隔2秒产生1个数字（从0开始递增1，无限个）
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "intervalRange开始subscribe连接");
                    }

                    // 默认最先调用复写的 onSubscribe（）
                    @Override
                    public void onNext(Long value) {
                        Log.d(TAG, "intervalRange接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "intervalRange对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "intervalRange对Complete事件作出响应");
                    }
                });

        // 参数1 = 事件序列起始点；
        // 参数2 = 事件数量；
        // 注：若设置为负数，则会抛出异常
        Observable.range(3, 10)
                // 该例子发送的事件序列特点：从3开始发送，每次发送事件递增1，一共发送10个事件
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "range开始subscribe连接");
                    }

                    // 默认最先调用复写的 onSubscribe（）
                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "range接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "range对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "range对Complete事件作出响应");
                    }
                });


    }
}
