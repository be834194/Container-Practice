create table todo (
    todoid   integer auto_increment primary key,
    todoname varchar (255) not null
);

insert into 
    todo_list.todo(todoid,todoname)
values
    (1,"服をクリーニングに出す"),
    (2,"きのこのマリネを作る");