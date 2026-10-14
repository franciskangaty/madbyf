package dev.madbyf.main.config;

import javax.sql.DataSource;

import org.axonframework.common.jdbc.UnitOfWorkAwareConnectionProviderWrapper;
import org.axonframework.common.transaction.NoTransactionManager;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.AggregateSnapshotter;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine;
import org.axonframework.messaging.annotation.HandlerDefinition;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.jdbc.SpringDataSourceConnectionProvider;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class AxonConfig {

    @Bean
    public EventStorageEngine eventStorageEngine(
            Serializer serializer,
            DataSource dataSource,
            TransactionManager transactionManager) {

        return JdbcEventStorageEngine.builder()
                .snapshotSerializer(serializer)
                .eventSerializer(serializer)
                .connectionProvider(
                    new UnitOfWorkAwareConnectionProviderWrapper(
                        new SpringDataSourceConnectionProvider(dataSource)
                    )
                )
                .transactionManager(transactionManager)
                .build();
    }
}
// @Configuration
// public class AxonConfig {


//     /**
//      * JDBC transaction manager for Axon event store.
//      */
//     // @Bean
//     // public DataSourceTransactionManager transactionManager(
//     //         DataSource dataSource) {

//     //     return new DataSourceTransactionManager(dataSource);
//     // }
//     // 
//     @Bean
//     public TransactionManager transactionManager() {
//         return NoTransactionManager.INSTANCE;
//     }

//     /**
//      * Axon event storage engine.
//      */
//     @Bean
//     public EventStorageEngine eventStorageEngine(
//             Serializer serializer,
//             DataSource dataSource,
//             TransactionManager transactionManager) {


//         return JdbcEventStorageEngine.builder()

//                 .snapshotSerializer(serializer)

//                 .eventSerializer(serializer)

//                 .connectionProvider(
//                     new UnitOfWorkAwareConnectionProviderWrapper(
//                         new SpringDataSourceConnectionProvider(dataSource)
//                     )
//                 )

//                 .transactionManager(
//                     new SpringTransactionManager(transactionManager)
//                 )

//                 .build();
//     }



//     @Bean
//     public EventStore eventStore(
//             EventStorageEngine storageEngine) {

//         return EmbeddedEventStore.builder()
//                 .storageEngine(storageEngine)
//                 .build();
//     }



//     @Bean
//     public Snapshotter snapshotter(
//             EventStore eventStore,
//             ParameterResolverFactory parameterResolverFactory,
//             HandlerDefinition handlerDefinition) {


//         return AggregateSnapshotter.builder()

//                 .eventStore(eventStore)

//                 .parameterResolverFactory(parameterResolverFactory)

//                 .handlerDefinition(handlerDefinition)

//                 .build();
//     }



//     @Bean
//     public SnapshotTriggerDefinition snapshotTriggerDefinition(
//             Snapshotter snapshotter) {


//         return new EventCountSnapshotTriggerDefinition(
//                 snapshotter,
//                 100
//         );
//     }

// }