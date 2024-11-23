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
                method.setAccessible(true); // private or protected funcs now public
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
            // a
            Path dir = Files.createDirectory(Paths.get(surname));

            // b
            Path file = Files.createFile(dir.resolve(name));

            // c
            Path dir1 = Files.createDirectories(dir.resolve("dir1"));
            Path dir2 = Files.createDirectories(dir.resolve("dir2"));
            Path dir3 = Files.createDirectories(dir.resolve("dir3"));
            Files.copy(file, dir1.resolve(name));
            Files.copy(file, dir2.resolve(name));
            Files.copy(file, dir3.resolve(name));

            // d
            Files.createFile(dir1.resolve("file1"));

            // e
            Files.createFile(dir2.resolve("file2"));

            // f
            System.out.println("Содержимое директории " + surname + ":");
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    System.out.println("F " + file.getFileName());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    System.out.println("D " + dir.getFileName());
                    return FileVisitResult.CONTINUE;
                }
            });

            // g
            deleteDirectory(dir1);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteDirectory(Path dir) throws IOException {
        try {
            Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
