package finley.gmair.config;

import finley.gmair.job.*;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
public class ScheduledConfiguration {

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();

        quartzScheduler.setDataSource(dataSource);
        quartzScheduler.setTransactionManager(transactionManager);
        quartzScheduler.setOverwriteExistingJobs(true);
        quartzScheduler.setSchedulerName("gmair-quartz-scheduler");

        // custom job factory of spring with DI support for @Autowired!
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        quartzScheduler.setJobFactory(jobFactory);

        quartzScheduler.setQuartzProperties(quartzProperties());

        Trigger[] triggers = { processHalfHourlyTrigger().getObject(), processHourlyTrigger().getObject(),
                processDailyTrigger().getObject(), processMonthlyTrigger().getObject() };
        quartzScheduler.setTriggers(triggers);

        return quartzScheduler;
    }

    @Bean(value = "halfHourlyJob")
    public JobDetailFactoryBean processHalfHourlyJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setJobClass(HalfHourlyJob.class);
        jobDetailFactory.setGroup("spring3-quartz");
        return jobDetailFactory;
    }

    @Bean(value = "halfHourlyTrigger")
    public CronTriggerFactoryBean processHalfHourlyTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(processHalfHourlyJob().getObject());
        cronTriggerFactoryBean.setCronExpression("0 0/30 * * * ?");
        cronTriggerFactoryBean.setGroup("spring3-quartz");
        return cronTriggerFactoryBean;
    }

    @Bean(value = "hourlyJob")
    public JobDetailFactoryBean processHourlyJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setJobClass(HourlyJob.class);
        jobDetailFactory.setGroup("spring3-quartz");
        return jobDetailFactory;
    }

    @Bean(value = "hourlyTrigger")
    public CronTriggerFactoryBean processHourlyTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(processHourlyJob().getObject());
        cronTriggerFactoryBean.setCronExpression("0 0 * * * ?");
        cronTriggerFactoryBean.setGroup("spring3-quartz");
        return cronTriggerFactoryBean;
    }

    @Bean(value = "dailyJob")
    public JobDetailFactoryBean processDailyJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setJobClass(DailyJob.class);
        jobDetailFactory.setGroup("spring3-quartz");
        return jobDetailFactory;
    }

    @Bean(value = "dailyTrigger")
    public CronTriggerFactoryBean processDailyTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(processDailyJob().getObject());
        cronTriggerFactoryBean.setCronExpression("0 0 0 * * ?");
        cronTriggerFactoryBean.setGroup("spring3-quartz");
        return cronTriggerFactoryBean;
    }

    @Bean(value = "dailyNoonJob")
    public JobDetailFactoryBean processDailyNoonJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setJobClass(DailyNoonJob.class);
        jobDetailFactory.setGroup("spring3-quartz");
        return jobDetailFactory;
    }

    @Bean(value = "dailyNoonTrigger")
    public CronTriggerFactoryBean processDailyNoonTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(processDailyJob().getObject());
        cronTriggerFactoryBean.setCronExpression("0 0 12 * * ?");
        cronTriggerFactoryBean.setGroup("spring3-quartz");
        return cronTriggerFactoryBean;
    }

    @Bean(value = "monthlyJob")
    public JobDetailFactoryBean processMonthlyJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setJobClass(MonthlyJob.class);
        jobDetailFactory.setGroup("spring3-quartz");
        return jobDetailFactory;
    }

    @Bean(value = "monthlyTrigger")
    public CronTriggerFactoryBean processMonthlyTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(processMonthlyJob().getObject());
        cronTriggerFactoryBean.setCronExpression("0 0 0 1 * ?");
        cronTriggerFactoryBean.setGroup("spring3-quartz");
        return cronTriggerFactoryBean;
    }

    @Bean
    public Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        Properties properties = null;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();

        } catch (IOException e) {
            System.out.println("cannot find quartz.properties");
        }

        return properties;
    }
}
