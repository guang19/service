package com.github.guang19.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>常用工具</p>
 */
public class CommonUtil
{
    /**
     * 判断对象是否为null
     * @param argName 参数名
     * @param arg     参数
     */
    public static void assertObjectNull(String argName,Object arg)
    {
        if(arg == null)
        {
            throw new NullPointerException(argName.concat(" can not be null"));
        }
    }

    /**
     * 判断所有参数是否为null
     * @param args 参数
     */
    public static void assertObjectsNull(Object ...args)
    {
        for (int i = 0 ; i < args.length ; ++i)
        {
            if(args[i] == null)
            {
                throw new NullPointerException("arg index of " + i + " can not be null");
            }
        }
    }

    /**
     * 判断数组是否为空
     * @param argName   数组名
     * @param args      参数
     */
    public static void assertArrayEmpty(String argName,Object[] args)
    {
        if(args == null || args.length <= 0)
        {
            throw new NullPointerException(argName.concat(" can not be empty"));
        }
    }

    /**
     * 判断集合是否为空
     * @param argName       集合名
     * @param collection    集合
     */
    public static void assertListEmpty(String argName,Collection collection)
    {
        if(collection == null || collection.size() <=0)
        {
            throw new NullPointerException(argName.concat(" can not be empty"));
        }
    }

    /**
     * 判断字符串是否以图片名结尾
     * @param str       字符串
     * @return          字符串
     */
    public static boolean endWithImgType(String str)
    {
        if(str != null)
        {
            return str.endsWith(".jpg") || str.endsWith(".png") || str.endsWith(".jpeg") || str.endsWith(".bmp") || str.endsWith(".gif") || str.endsWith(".tif")
                    || str.endsWith(".webp") || str.endsWith(".pcx") || str.endsWith(".tga") || str.endsWith(".sdr") || str.endsWith(".pcd") || str.endsWith(".dxf")
                    || str.endsWith(".WMF") || str.endsWith(".raw");
        }
        return false;
    }

    /**
     * <p>判断localDir是否存在并且的确为一个文件夹</p>
     * @param localDir  本地文件夹
     */
    public static void checkLocalDir(String localDir)
    {
        if(localDir == null)
        {
            throw new IllegalArgumentException("local directory  cannot be null");
        }
        Path path = Path.of(localDir);
        if(Files.notExists(path))
        {
            throw new IllegalArgumentException("local directory is not exist");
        }
        if(!Files.isDirectory(path))
        {
            throw new IllegalArgumentException("local directory must be a directory,can not be a file");
        }
    }

    /**
     * <p>判断指定的文件路径是否存在并且是否为一个文件</p>
     * @param filePath  文件路径
     * @return          文件路径是否存在并且是否为一个文件
     */
    public static Path checkLocalFile(String filePath)
    {
        if(filePath == null || filePath.isBlank())
        {
            throw new IllegalArgumentException("file cannot be null");
        }
        Path path = Path.of(filePath);
        if(Files.notExists(path))
        {
            throw new IllegalArgumentException("file is not exist");
        }
        if(Files.isDirectory(path))
        {
            throw new IllegalArgumentException("file must be a file,can not be directory");
        }
        return path;
    }

    /**
     * <p>创建文件</p>
     * @param file  文件路径
     * @return      创建好的文件
     */
    public static File createFile(String file) throws IOException
    {
        Path path = Path.of(file);
        Path parent = path.getParent();
        //如果父目录存在就直接创建当前文件
        if(Files.exists(parent))
        {
            Files.createFile(path);
        }
        //如果父目录不存在就先创建父目录再创建当前文件
        else
        {
            Files.createDirectories(parent);
            Files.createFile(path);
        }
        return path.toFile();
    }
}
