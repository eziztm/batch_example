package com.annagurban.eziz.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
    
    
      // Execute jobs separately  
//    public static void main(String[] args) throws JobExecutionAlreadyRunningException,
//            InterruptedException,
//            JobRestartException,
//            JobInstanceAlreadyCompleteException,
//            JobParametersInvalidException,
//            IOException {
//
//        String jobparam = "";
//
//        if (args.length > 0) {
//            jobparam = args[0];
//        } else {
//            System.out.println("No job name given");
//            return;
//        }
//
//        switch (jobparam) {
//            case "-singleLine":
//                executeJob("singleLineJob");
//                break;
//            case "-csvToXml":
//                executeJob("csvToXmlJob");
//                break;
//            case "-xmlToDb":
//                executeJob("xmlToDbJob");
//                break;
//        }
//    }
//
//    private static void executeJob(String jobName)
//            throws JobExecutionAlreadyRunningException, InterruptedException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
//
//        SpringApplication app = new SpringApplication(Application.class);
//        app.setWebEnvironment(false);
//        //ConfigurableApplicationContext ctx = app.run(args);  
//        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
//        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
//
//        Job job = ctx.getBean(jobName, Job.class);
//
//        JobParameters jobParameters = new JobParametersBuilder()
//                //.addDate("date", new Date())
//                .toJobParameters();
//
//        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
//    }
}
