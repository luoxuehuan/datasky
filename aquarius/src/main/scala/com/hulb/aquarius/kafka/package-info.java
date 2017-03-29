/**
 * Created by hulb on 17/3/28.
 *
 *为什么使用消息系统
 *
 * 解耦
 * 冗余
 * 扩散性
 * 灵活性 峰值处理能力
 * 可恢复性
 * 送达保证
 * 顺序保证
 * 缓冲
 * 理解数据流
 * 异步通信
 *
 * 名词解释：
 *
 * 1. Broker
 *          Kafka集群包含一个或多个服务器，这种服务器被称为broker
 * 2. Topic
 *          每条发布到Kafka集群的消息都有一个类别，这个类别被称为topic
 * 3. Partition：
 *          parition是物理上的概念，物理上把topic分成一个或多个partition
 *          每个topic包含一个或多个partition，创建topic时可指定parition数量。
 *          每个partition对应于一个文件夹，该文件夹下存储该partition的①数据(消息,log,log entries)和②索引文件
 *              ①数据
 *              ②索引
 * 4. Producer
 *          负责发布消息到Kafka broker
 * 5. Consumer
 *          每个consumer属于一个特定的consumer group
 * 6. Group
 *          使用consumer high level API时，
 *          同一topic的一条消息只能被同一个consumer group内的一个consumer消费，
 *          但多个consumer group可同时消费这一消息。
 *          一个topic 3个partition的话，一个group 里面3个consumer 可消费3个 partition 消息。 如果group 出现第四个consumer ,则消费不到消息。
 *
 * 7. Replication
 *
 *
 * 8. leader election
 *
 * 9. consumer rebalance
 *
 *
 * Why quick
 * 1.不需要标记哪些消息被哪些consumer过，不需要通过broker去保证同一个consumer group只有一个consumer能消费某一条消息，因此也就不需要锁机制
 * 2.
 */
package com.hulb.aquarius.kafka;

