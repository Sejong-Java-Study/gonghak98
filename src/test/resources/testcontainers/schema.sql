create table AbeekDomain (
                             abeekType tinyint not null check (abeekType between 0 and 4),
                             minCredit integer not null,
                             year integer not null check ((year>=14) and (year<=24)),
                             id bigint not null auto_increment,
                             major_id bigint,
                             note tinytext,
                             primary key (id)
);

create table CompletedCoursesDomain (
                                        semester integer not null check ((semester>=1) and (semester<=2)),
                                        year integer not null check ((year>=15) and (year<=24)),
                                        course_id bigint,
                                        id bigint not null auto_increment,
                                        user_id bigint,
                                        primary key (id)
);

create table CoursesDomain (
                               credit integer not null,
                               courseId bigint not null,
                               name varchar(255) not null,
                               primary key (courseId)
);

create table GonghakCoursesDomain (
                                      designCredit float not null check ((designCredit>=0) and (designCredit<=6)),
                                      year integer not null check ((year>=15) and (year<=24)),
                                      course_id bigint,
                                      id bigint not null auto_increment,
                                      major_id bigint,
                                      courseCategory varchar(255) not null,
                                      passCategory varchar(255) not null,
                                      primary key (id)
);

create table MajorsDomain (
                              id bigint not null,
                              major varchar(255) not null,
                              primary key (id)
);

create table UserDomain (
                            id bigint not null auto_increment,
                            major_id bigint,
                            studentId bigint not null,
                            email varchar(255) not null,
                            name varchar(255) not null,
                            password varchar(255) not null,
                            primary key (id)
);

alter table AbeekDomain
add constraint FKqp6ay2sjkbn4hooguigj9mqnv
foreign key (major_id)
references MajorsDomain (id);

alter table CompletedCoursesDomain
add constraint FKhx1h6qt61i3npeib86gd64xp4
foreign key (course_id)
references CoursesDomain (courseId);

alter table CompletedCoursesDomain
add constraint FKgutgwcobexlotyfvbrlr7f7og
foreign key (user_id)
references UserDomain (id);

alter table GonghakCoursesDomain
add constraint FKdk1l4utu4dfavyahxafqeijw4
foreign key (course_id)
references CoursesDomain (courseId);

alter table GonghakCoursesDomain
add constraint FK38sqoiv1lj2ucn2rgehl1bc0l
foreign key (major_id)
references MajorsDomain (id);

alter table UserDomain
add constraint FKh05bg3njuiwsvmcvssvpfyoki
foreign key (major_id)
references MajorsDomain (id);
