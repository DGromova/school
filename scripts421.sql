--- Возраст студента не может быть меньше 12 лет.
--- Имена студентов должны быть уникальными и не равны нулю.
--- Пара “значение названия” - “цвет факультета” должна быть уникальной.
--- При создании студента без возраста ему автоматически должно присваиваться 20 лет.

ALTER TABLE student ADD CONSTRAINT age_more_than_12 CHECK (age > 12);

ALTER TABLE student ADD CONSTRAINT name_unique UNIQUE (name);

ALTER TABLE student ALTER COLUMN name SET NOT NULL;

ALTER TABLE faculty ADD CONSTRAINT name_color_unique UNIQUE (name, color);