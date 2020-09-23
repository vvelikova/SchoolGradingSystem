package com.vvelikova.schoolgradingsystem.batchConfigurations;

import com.vvelikova.schoolgradingsystem.additionalConfigurations.JpaConfig;
import com.vvelikova.schoolgradingsystem.domain.Course;
import com.vvelikova.schoolgradingsystem.domain.Student;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.batch.item.file.FlatFileItemReader;


@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JpaConfig dataSource;


    @Bean
    public Step loadStudents() {
        return stepBuilderFactory.get("loadStudents").<Student, Student>chunk(17000)
                .reader(studentReader())
                .processor(studentProcessor()) // -> if i have to do additional modification of the data before passing to the db
                .writer(studentWriter())
                .build();
    }

    @Bean
    public Step loadCourses() {
        return stepBuilderFactory.get("loadCourses").<Course, Course>chunk(17000)
                .reader(courseReader())
                .processor(courseProcessor()) // -> if i have to do additional modification of the data before passing to the db
                .writer(courseWriter())
                .build();
    }

    @Bean
    public Job importDataJob() {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(loadStudents())
//                .start(loadStudents())
//                .next(loadCourses())
//                .flow(loadCourses())
                .end()
                .build();
    }

    @Bean
    public ItemProcessor<Student, Student> studentProcessor() {
        return new StudentItemProcessor();
    }

    @Bean
    public ItemProcessor<Course, Course> courseProcessor() {
        return new CourseItemProcessor();
    }


    @Bean
    public FlatFileItemReader<Student> studentReader() {
        FlatFileItemReader<Student> itemReader = new FlatFileItemReader<Student>();
        itemReader.setLineMapper(studentLineMapper());
        itemReader.setLinesToSkip(1);
        itemReader.setMaxItemCount(17000);
        itemReader.setResource(new FileSystemResource("input/OriginalMarks.csv"));
        return itemReader;
    }

    @Bean
    public FlatFileItemReader<Course> courseReader() {
        FlatFileItemReader<Course> itemReaderCourse = new FlatFileItemReader<Course>();
        itemReaderCourse.setLineMapper(courseLineMapper());
        itemReaderCourse.setLinesToSkip(1);
        itemReaderCourse.setMaxItemCount(17000);
        itemReaderCourse.setResource(new FileSystemResource("input/OriginalMarks.csv"));
        return itemReaderCourse;
    }

    @Bean
    public LineMapper<Student> studentLineMapper() {
        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<Student>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
//        lineTokenizer.setNames(new String[]{"test_id", "id", "student_name", "course_id", "course_name", "mark_id", "mark"});
//        lineTokenizer.setNames(new String[]{"test_id", "id", "student_name"});
        lineTokenizer.setIncludedFields(new int[]{0, 1}); // which fileds from the input to take into account
        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<Student>();
        fieldSetMapper.setTargetType(Student.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
                    Student studentTemp = new Student();
                    studentTemp.setCsvId(fieldSet.readLong(0));
                    studentTemp.setStudentName(fieldSet.readString(1));
                    studentTemp.setFromCSV(true);

                    return studentTemp;
                });
        return lineMapper;
    }

    @Bean
    public LineMapper<Course> courseLineMapper() {
        DefaultLineMapper<Course> lineMapper = new DefaultLineMapper<Course>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setIncludedFields(new int[]{2, 3}); // which fileds from the input to take into account
        BeanWrapperFieldSetMapper<Course> fieldSetMapper = new BeanWrapperFieldSetMapper<Course>();
        fieldSetMapper.setTargetType(Course.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            Course courseTemp = new Course();
            courseTemp.setCsvId(fieldSet.readLong(0));
            courseTemp.setCourseName(fieldSet.readString(1));
            courseTemp.setFromCSV(true);

            System.out.println("COurse to String: \n" + courseTemp);

            return courseTemp;
        });
        return lineMapper;
    }

    @Bean
    public JdbcBatchItemWriter<Student> studentWriter(){
        JdbcBatchItemWriter<Student> writer = new JdbcBatchItemWriter<Student>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Student>());
        writer.setSql("INSERT INTO student(student_name, csv_id, fromcsv) VALUES (:studentName, :csvId, :fromCSV)");
        writer.setDataSource(dataSource.getDataSource());

        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<Course> courseWriter(){

        System.out.println("In courseWriter!!!");
        JdbcBatchItemWriter<Course> writer = new JdbcBatchItemWriter<Course>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Course>());
        writer.setSql("INSERT INTO course(course_name, csv_id, fromcsv) VALUES (:courseName, :csvId, :fromCSV)");
        writer.setDataSource(dataSource.getDataSource());

        return writer;
    }
}
