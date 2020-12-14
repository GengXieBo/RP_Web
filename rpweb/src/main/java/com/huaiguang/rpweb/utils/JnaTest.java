package com.huaiguang.rpweb.utils;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;


public class JnaTest {
    public interface RnnLibrary extends Library {
        RnnLibrary INSTANCE = (RnnLibrary) Native.load("rnnPredict.dll", RnnLibrary.class);
        void setCudaVisibleDevices(String id);
        void setAdditionalPath(String path);
        Pointer initializeHandleJava(String ini_path);
        interface UpdateProgressFunc extends Callback {
            void invoke(int stage, int w, int h, int currentIndex);
        }
        /**
         * 处理一个切片
         * @param handle:通过initializeHandleJava获取到的模型的句柄
         * @param slide_path:切片的路径
         * @param annos:返回的标注
         * @param len:需要推荐的数量
         * @param whole_score:整张切片的分数
         * @param callback:进度的回调
         * @param savepath:结果保存路径，会保存len个推荐区域，以及高层级的一个图像。
         * @return:是否正确运行
         */
        boolean slideProcessJava(Pointer handle, String slide_path, Anno[] annos, int[] len, double[] whole_score,
                                 UpdateProgressFunc callback, String savepath);
        void freeModelMemJava(Pointer handle);
    }

    private static Pointer pointer;
    public static RnnLibrary.UpdateProgressFunc updateProgressFunc = new RnnLibrary.UpdateProgressFunc(){
        public void invoke(int stage, int w, int h, int currentIndex) {
            System.out.println("stage: "+stage+"progress: "+currentIndex+"/"+w*h);
        }
    };

    public static void getHandle() {
        System.setProperty("jna.library.path", "D:\\IDEAProjects\\RP_Web\\rpweb\\src\\main\\resources\\dll");
        RnnLibrary.INSTANCE.setAdditionalPath("D:\\IDEAProjects\\RP_Web\\rpweb\\src\\main\\resources\\dll");
        RnnLibrary.INSTANCE.setCudaVisibleDevices("0");
        System.setProperty("jna.debug_load", "true");
        pointer = RnnLibrary.INSTANCE.initializeHandleJava("D:\\IDEAProjects\\RP_Web\\rpweb\\src\\main\\resources\\dll\\config.ini");
    }

    public static double slideProcess(int recom, String slide_path, String save_path) {
        if(pointer == null)
        {
            System.out.println("slide pointer is null");
            return 0;
        }
        Anno anno = new Anno();
        Anno[] annos = (Anno[])anno.toArray(recom);
        int[] len = new int[1];
        len[0] = recom;
        double[] whole_score = new double[1];
        whole_score[0] = 0;

        boolean flag = RnnLibrary.INSTANCE.slideProcessJava(
                pointer, slide_path, annos, len, whole_score, updateProgressFunc, save_path);
        double score = whole_score[0];
        return score;
    }

    public static void free() {
        RnnLibrary.INSTANCE.freeModelMemJava(pointer);
    }

}
