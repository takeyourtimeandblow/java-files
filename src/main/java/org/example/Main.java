package org.example;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.io.File;


public class Main {
    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        Method[] methods = MyClass.class.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(InvokeTimes.class)) {
                InvokeTimes annotation = method.getAnnotation(InvokeTimes.class);
                int times = annotation.value();
                method.setAccessible(true); // Делаем приватные методы доступными
                for (int i = 0; i < times; i++) {
                    try {
                        method.invoke(myClass);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        String surname = "Ermalyuk";
        String name = "Egor";

        try {
            // a. Создаем директорию <surname>
            Path dir = Files.createDirectory(Paths.get(surname));

            // b. Создаем файл <name> внутри директории <surname>
            Path file = Files.createFile(dir.resolve(name));

            // c. Создаем вложенные директории dir1/ dir2/ dir3 и копируем файл
            Path dir1 = Files.createDirectories(dir.resolve("dir1"));
            Path dir2 = Files.createDirectories(dir.resolve("dir2"));
            Path dir3 = Files.createDirectories(dir.resolve("dir3"));
            Files.copy(file, dir1.resolve(name));
            Files.copy(file, dir2.resolve(name));
            Files.copy(file, dir3.resolve(name));

            // d. Создаем файл file1 внутри dir1
            Files.createFile(dir1.resolve("file1"));

            // e. Создаем файл file2 внутри dir2
            Files.createFile(dir2.resolve("file2"));

            // f. Рекурсивный обход директории <surname>
            System.out.println("Содержимое директории " + surname + ":");
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("F " + file.getFileName());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    System.out.println("D " + dir.getFileName());
                    return FileVisitResult.CONTINUE;
                }
            });

            // g. Удаляем директорию dir1 со всем ее содержимым
            deleteDirectory(dir1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteDirectory(Path dir) throws IOException {
        Files.walk(dir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}

