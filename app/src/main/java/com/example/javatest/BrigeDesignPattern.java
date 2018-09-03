package com.example.javatest;

public class BrigeDesignPattern {
    static abstract class AbstractTeacher {
        private ICourse mCourse;
        private String mName;

        public AbstractTeacher(String name, ICourse course) {
            mCourse = course;
            mName = name;
        }

        public ICourse getCourse() {
            return mCourse;
        }

        public abstract void doing();

        public String getName(){
            return mName;
        }

        public void setCourse(ICourse course) {
            mCourse = course;
        }
    }

    interface ICourse{
        String courseName();
    }

    static class XiaoMing extends AbstractTeacher {

        public XiaoMing(ICourse course) {
            super("小明", course);
        }

        @Override
        public void doing() {
            System.out.println(getName() + "老师正在教" + getCourse().courseName());
        }
    }

    static class XiaoHong extends AbstractTeacher{

        public XiaoHong(ICourse course) {
            super("小红", course);
        }

        @Override
        public void doing() {
            System.out.println(getName() + "老师正在批改" + getCourse().courseName() + "试卷");
        }
    }

    static class Math implements ICourse{

        @Override
        public String courseName() {
            return "数学课";
        }
    }

    static class Chinese implements ICourse{

        @Override
        public String courseName() {
            return "语文课";
        }
    }

    public static void main(String[] args){
        ICourse math = new Math();
        ICourse chinese = new Chinese();
        AbstractTeacher xiaoming = new XiaoMing(math);
        AbstractTeacher xiaohong = new XiaoHong(chinese);
        xiaoming.doing();
        xiaohong.doing();

        xiaohong.setCourse(math);
        xiaoming.setCourse(chinese);

        xiaohong.doing();
        xiaoming.doing();
    }


}
