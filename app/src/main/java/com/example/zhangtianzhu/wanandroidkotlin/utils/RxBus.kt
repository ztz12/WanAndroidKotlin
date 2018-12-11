package com.example.zhangtianzhu.wanandroidkotlin.utils

import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

class RxBus
/**
 * PublishSubject只会把在订阅发生的时间点之后来自原始Flowable的数据发射给观察者
 */
private constructor() {

    /**
     * 主题
     */
    private val bus: FlowableProcessor<Any> = PublishProcessor.create<Any>().toSerialized()

    private object RxBusHolder {
         val INSTANCE = RxBus()
    }

    /**
     * 提供了一个新的事件
     *
     * @param o Object
     */
    fun post(o: Any) {
        bus.onNext(o)
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者 Flowable,Rxjava2中出现替代Observable
     *
     * @param eventType Event type
     * @param <T> 对应的Class类型
     * @return Flowable<T>
    </T></T> */
    fun <T> toFlowable(eventType: Class<T>): Flowable<T> {
        return bus.ofType(eventType)
    }

    companion object {

        val default: RxBus
            get() = RxBusHolder.INSTANCE
    }


}
