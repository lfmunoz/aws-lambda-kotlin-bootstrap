package com.github.kevinrob

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mguenther.kafka.junit.EmbeddedKafkaCluster
import net.mguenther.kafka.junit.EmbeddedKafkaClusterConfig
import net.mguenther.kafka.junit.ExternalKafkaCluster
import net.mguenther.kafka.junit.ObserveKeyValues.on
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class KafkaTest {

    private lateinit var kafka: EmbeddedKafkaCluster

    @BeforeEach
    fun setupKafka() {
        kafka = EmbeddedKafkaCluster.provisionWith(EmbeddedKafkaClusterConfig.defaultClusterConfig())
        kafka.start()
    }

    @AfterEach
    fun tearDownKafka() {
        kafka.stop()
    }

    @Test
    @Throws(Exception::class)
    fun shouldWaitForRecordsToBePublished() {
        kafka.send(net.mguenther.kafka.junit.SendValues.to("test-topic", "a", "b", "c"))
        kafka.observe(on("test-topic", 3))


    }

    @Test
    fun publisher() : Unit  = runBlocking {

        val kafkaConfig = KafkaConfig(
            bootstrapServer = kafka.brokerList,
            topic = "test-topic",
            groupId = "groupId"
        )

        val publisher = KafkaPublisherStateful(kafkaConfig)
        repeat(5) {
            val metadata = publisher.send(KafkaMessage("test".toByteArray(), "test".toByteArray()))
            println(metadata)
        }

        kafka.observe(on("test-topic", 1))


    }
}