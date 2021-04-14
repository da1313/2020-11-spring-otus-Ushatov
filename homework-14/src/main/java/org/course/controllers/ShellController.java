package org.course.controllers;

import org.course.service.FakeNosqlDataHandler;
import org.course.service.FakeSqlDataHandler;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShellController {

    private final JobLauncher jobLauncher;

    private final Job sqlToNosqlJob;

    private final Job nosqlToNosqlJob;

    private final FakeSqlDataHandler fakeSqlDataHandler;

    private final FakeNosqlDataHandler fakeNosqlDataHandler;

    public ShellController(JobLauncher jobLauncher,
                           @Qualifier("sqlToNosqlJob") Job sqlToNosqlJob,
                           @Qualifier("nosqlToSqlJob") Job nosqlToNosqlJob,
                           FakeSqlDataHandler fakeSqlDataHandler,
                           FakeNosqlDataHandler fakeNosqlDataHandler) {
        this.jobLauncher = jobLauncher;
        this.sqlToNosqlJob = sqlToNosqlJob;
        this.nosqlToNosqlJob = nosqlToNosqlJob;
        this.fakeSqlDataHandler = fakeSqlDataHandler;
        this.fakeNosqlDataHandler = fakeNosqlDataHandler;
    }

    @ShellMethod(value = "Initialize sql data", key = {"init-sql", "isql"})
    public String initSqlData(){
        fakeSqlDataHandler.initData();
        return "done";
    }

    @ShellMethod(value = "Delete sql data", key = {"del-sql", "dsql"})
    public String deleteSqlData(){
        fakeSqlDataHandler.clearData();
        return "done";
    }

    @ShellMethod(value = "Initialize nosql data", key = {"init-nosql", "insql"})
    public String initNosqlData(){
        fakeNosqlDataHandler.initData();
        return "done";
    }

    @ShellMethod(value = "Delete nosql data", key = {"del-nosql", "dnsql"})
    public String deleteNosqlData(){
        fakeNosqlDataHandler.clearData();
        return "done";
    }

    @ShellMethod(value = "Migrate to nosql", key = {"m-nosql"})
    public String migrateToNosql(long pageSize) throws Exception{
        jobLauncher.run(sqlToNosqlJob, new JobParametersBuilder()
                .addLong("pageSize", pageSize).toJobParameters());
        return "done";
    }

    @ShellMethod(value = "Migrate to sql", key = {"m-sql"})
    public String migrateToSql(long pageSize) throws Exception{
        jobLauncher.run(nosqlToNosqlJob, new JobParametersBuilder()
                .addLong("pageSize", pageSize).toJobParameters());
        return "done";
    }

}
