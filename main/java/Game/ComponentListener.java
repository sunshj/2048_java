package Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

public class ComponentListener extends KeyAdapter implements ActionListener {

    public JLabel lb;
    public JButton bt, about, back;
    public JCheckBox isSoundBox;
    int score = 0;
    int tempscore, tempscore2;//记录回退的分数值

    private GameStart UI;// 界面对象
    private int Numbers[][];// 存放数据的数组
    private Random rand = new Random();
    private int BackUp[][] = new int[4][4];//用于备份数组，供回退时使用
    private int BackUp2[][] = new int[4][4];//用于备份数组，供起死回生时使用
    //是否胜利,true:胜利，false：失败
    private boolean isWin = false;
    //是否复活，true：使用复活，false:不使用复活
    private boolean relive = false;
    //是否可以回退，true：不可回退，false：可以回退  (是否已经进行过一次回退了)
    private boolean hasBack = false;
    //是否播放音乐，true:播放音效，false：不播放音效
    private boolean isSound = true;

    public ComponentListener(GameStart UI, int Numbers[][], JLabel lb, JButton bt, JButton about, JButton back, JCheckBox isSoundBox) {
        this.UI = UI;
        this.Numbers = Numbers;
        this.lb = lb;
        this.bt = bt;
        this.about = about;
        this.back = back;
        this.isSoundBox = isSoundBox;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bt) {
            //游戏开始
            isWin = false;
            //各个小格赋初值0
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    Numbers[i][j] = 0;
            //游戏开始，分数为0
            score = 0;
            lb.setText("分数:" + score);
            //生成4个0-3之间的随机数
            int r1 = rand.nextInt(4);
            int r2 = rand.nextInt(4);
            int c1 = rand.nextInt(4);
            int c2 = rand.nextInt(4);
            //由r1,c1;r2,c2组成两个初始值，所以初始值的坐标不能重复
            while (r1 == r2 && c1 == c2) {
                r2 = rand.nextInt(4);
                c2 = rand.nextInt(4);
            }
            // 生成初始数字（2或者4）
            int value1 = rand.nextInt(2) * 2 + 2;
            int value2 = rand.nextInt(2) * 2 + 2;

            // 把数字存进对应的位置
            Numbers[r1][c1] = value1;
            Numbers[r2][c2] = value2;
            //数字更改，重新绘制图形，为此组件创建图形上下文
            UI.paint(UI.getGraphics());
        } else if (e.getSource() == about) {
            //点击了关于标签
            JOptionPane.showMessageDialog(UI, "游戏规则：\n"
                    + "1、开始时棋盘内随机出现两个数字，出现的数字仅可能为2或4\n"
                    + "2、玩家可以选择上下左右四个方向，若棋盘内的数字出现位移或合并，视为有效移动\n"
                    + "3、玩家选择的方向上若有相同的数字则合并，每次有效移动可以同时合并，但不可以连续合并\n"
                    + "4、合并所得的所有新生成数字相加即为该步的有效得分\n"
                    + "5、玩家选择的方向行或列前方有空格则出现位移\n"
                    + "6、每有效移动一步，棋盘的空位(无数字处)随机出现一个数字(依然可能为2或4)\n"
                    + "7、棋盘被数字填满，无法进行有效移动，判负，游戏结束\n"
                    + "8、棋盘上出现2048，判胜，游戏结束。\n"
            );
        } else if (e.getSource() == back && hasBack == false) {
            System.out.println("回退");
            //点击了回退一步标签，而且只能回退一次，只有再执行一次上下左右操作才可以再次回退
            hasBack = true;
            //判断本次回退是回退上一步，还是复活，回退上上步
            if (relive == false) {
                //替换上一步的分数
                score = tempscore;
                lb.setText("分数:" + score);
                for (int i = 0; i < BackUp.length; i++) {
                    Numbers[i] = Arrays.copyOf(BackUp[i], BackUp[i].length);
                }
            } else {
                //选择了起死回生
                score = tempscore2;
                lb.setText("分数:" + score);
                for (int i = 0; i < BackUp2.length; i++) {
                    Numbers[i] = Arrays.copyOf(BackUp2[i], BackUp2[i].length);
                }
                //再给一次复活的机会
                relive = false;
            }
            //重新绘制
            UI.paint(UI.getGraphics());
        } else if (e.getSource().equals(isSoundBox)) {
            //是否选中静音复选框
            if (isSoundBox.isSelected()) {
                isSound = false;
            } else {
                isSound = true;
            }
        }
    }


    // 键盘监听，监听游戏焦点的←，↑，→，↓;方向键键值：左：37上：38右：39下：40
    public void keyPressed(KeyEvent event) {

        int Counter = 0;// 记录数字有效移动位数，判断是否移动了
        int NumCounter = 0;// 记录当前有数字的小方格数量，判断是否已满
        int NumNearCounter = 0;// 记录相邻格子数字相同的对数

        hasBack = false;
        //每次进行真正的移位合并操作之前，记录前一步

        //记录上上步
        if (BackUp != null || BackUp.length != 0) {
            tempscore2 = tempscore;// 先把分数备份好
            // 下面的for循环调用java.util.Arrays.copyOf()方法复制数组，实现备份
            for (int i = 0; i < BackUp.length; i++) {
                BackUp2[i] = Arrays.copyOf(BackUp[i], BackUp[i].length);
            }
        }
        //记录上步
        tempscore = score;// 先把分数备份好
        // 下面的for循环调用java.util.Arrays.copyOf()方法复制数组，实现备份
        for (int i = 0; i < Numbers.length; i++) {
            BackUp[i] = Arrays.copyOf(Numbers[i], Numbers[i].length);
        }

        if (isWin == false) {
            switch (event.getKeyCode()) {

                case 37:
                    // 向左移动
                    if (isSound == true) {
                        new PlaySound("src/res/move.wav").start();// 播放移位音乐
                    }
                    //经过这个循环，把每行有值的格子，都被搬到最左边了，同一行右侧有值的格子，覆盖左侧值为0的格子
                    for (int h = 0; h < 4; h++)
                        for (int l = 0; l < 4; l++)
                            if (Numbers[h][l] != 0) {
                                int temp = Numbers[h][l];
                                int pre = l - 1;
                                while (pre >= 0 && Numbers[h][pre] == 0) {
                                    Numbers[h][pre] = temp;
                                    Numbers[h][pre + 1] = 0;
                                    pre--;
                                    Counter++;
                                }
                            }
                    //表盘当前左侧相邻相等的值会相加，造成左边值为【和】，相邻右边值为【0】
                    for (int h = 0; h < 4; h++)
                        for (int l = 0; l < 4; l++)
                            if (l + 1 < 4
                                    && (Numbers[h][l] == Numbers[h][l + 1])
                                    && (Numbers[h][l] != 0 || Numbers[h][l + 1] != 0)) {
                                if (isSound == true)
                                    new PlaySound("src/res/merge.wav").start();
                                Numbers[h][l] = Numbers[h][l] + Numbers[h][l + 1];
                                Numbers[h][l + 1] = 0;
                                Counter++;
                                score += Numbers[h][l];
                                if (Numbers[h][l] == 2048) {
                                    isWin = true;
                                }
                            }
                    //经过这个循环，把每行有值的格子，都被搬到最左边了，同一行右侧有值的格子，覆盖左侧值为0的格子
                    for (int h = 0; h < 4; h++)
                        for (int l = 0; l < 4; l++)
                            if (Numbers[h][l] != 0) {
                                int temp = Numbers[h][l];
                                int pre = l - 1;
                                while (pre >= 0 && Numbers[h][pre] == 0) {
                                    Numbers[h][pre] = temp;
                                    Numbers[h][pre + 1] = 0;
                                    pre--;
                                    Counter++;
                                }
                            }
                    break;

                case 39:// 向右移动
                    if (isSound == true)
                        new PlaySound("src/res/move.wav").start();
                    for (int h = 3; h >= 0; h--)
                        for (int l = 3; l >= 0; l--)
                            if (Numbers[h][l] != 0) {
                                int temp = Numbers[h][l];
                                int pre = l + 1;
                                while (pre <= 3 && Numbers[h][pre] == 0) {
                                    Numbers[h][pre] = temp;
                                    Numbers[h][pre - 1] = 0;
                                    pre++;
                                    Counter++;
                                }
                            }

                    for (int h = 3; h >= 0; h--)
                        for (int l = 3; l >= 0; l--)
                            if (l + 1 < 4
                                    && (Numbers[h][l] == Numbers[h][l + 1])
                                    && (Numbers[h][l] != 0 || Numbers[h][l + 1] != 0)) {
                                if (isSound == true)
                                    new PlaySound("src/res/merge.wav").start();
                                Numbers[h][l + 1] = Numbers[h][l]
                                        + Numbers[h][l + 1];
                                Numbers[h][l] = 0;
                                Counter++;
                                score += Numbers[h][l + 1];
                                if (Numbers[h][l + 1] == 2048) {
                                    isWin = true;
                                }
                            }
                    for (int h = 3; h >= 0; h--)
                        for (int l = 3; l >= 0; l--)
                            if (Numbers[h][l] != 0) {
                                int temp = Numbers[h][l];
                                int pre = l + 1;
                                while (pre <= 3 && Numbers[h][pre] == 0) {
                                    Numbers[h][pre] = temp;
                                    Numbers[h][pre - 1] = 0;
                                    pre++;
                                    Counter++;
                                }
                            }
                    break;

                case 38:
                    // 向上移动
                    if (isSound == true)
                        new PlaySound("src/res/move.wav").start();
                    for (int l = 0; l < 4; l++)
                        for (int h = 0; h < 4; h++)
                            if (Numbers[h][l] != 0) {
                                int temp = Numbers[h][l];
                                int pre = h - 1;
                                while (pre >= 0 && Numbers[pre][l] == 0) {
                                    Numbers[pre][l] = temp;
                                    Numbers[pre + 1][l] = 0;
                                    pre--;
                                    Counter++;
                                }
                            }
                    for (int l = 0; l < 4; l++)
                        for (int h = 0; h < 4; h++)
                            if (h + 1 < 4
                                    && (Numbers[h][l] == Numbers[h + 1][l])
                                    && (Numbers[h][l] != 0 || Numbers[h + 1][l] != 0)) {
                                if (isSound == true)
                                    new PlaySound("src/res/merge.wav").start();
                                Numbers[h][l] = Numbers[h][l] + Numbers[h + 1][l];
                                Numbers[h + 1][l] = 0;
                                Counter++;
                                score += Numbers[h][l];
                                if (Numbers[h][l] == 2048) {
                                    isWin = true;
                                }
                            }

                    for (int l = 0; l < 4; l++)
                        for (int h = 0; h < 4; h++)
                            if (Numbers[h][l] != 0) {
                                int temp = Numbers[h][l];
                                int pre = h - 1;
                                while (pre >= 0 && Numbers[pre][l] == 0) {
                                    Numbers[pre][l] = temp;
                                    Numbers[pre + 1][l] = 0;
                                    pre--;
                                    Counter++;
                                }
                            }
                    break;

                case 40:
                    // 向下移动
                    if (isSound == true)
                        new PlaySound("src/res/move.wav").start();
                    for (int l = 3; l >= 0; l--)
                        for (int h = 3; h >= 0; h--)
                            if (Numbers[h][l] != 0) {
                                int temp = Numbers[h][l];
                                int pre = h + 1;
                                while (pre <= 3 && Numbers[pre][l] == 0) {
                                    Numbers[pre][l] = temp;
                                    Numbers[pre - 1][l] = 0;
                                    pre++;
                                    Counter++;
                                }
                            }
                    for (int l = 3; l >= 0; l--)
                        for (int h = 3; h >= 0; h--)
                            if (h + 1 < 4
                                    && (Numbers[h][l] == Numbers[h + 1][l])
                                    && (Numbers[h][l] != 0 || Numbers[h + 1][l] != 0)) {
                                if (isSound == true)
                                    new PlaySound("src/res/merge.wav").start();
                                Numbers[h + 1][l] = Numbers[h][l]
                                        + Numbers[h + 1][l];
                                Numbers[h][l] = 0;
                                Counter++;
                                score += Numbers[h + 1][l];
                                if (Numbers[h + 1][l] == 2048) {
                                    isWin = true;
                                }
                            }

                    for (int l = 3; l >= 0; l--)
                        for (int h = 3; h >= 0; h--)
                            if (Numbers[h][l] != 0) {
                                int temp = Numbers[h][l];
                                int pre = h + 1;
                                while (pre <= 3 && Numbers[pre][l] == 0) {
                                    Numbers[pre][l] = temp;
                                    Numbers[pre - 1][l] = 0;
                                    pre++;
                                    Counter++;
                                }
                            }
                    break;

            }
            //移位，合并，移位完成后，判断是否有可重复值
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (Numbers[i][j] == Numbers[i][j + 1]
                            && Numbers[i][j] != 0) {
                        NumNearCounter++;
                    }
                    if (Numbers[i][j] == Numbers[i + 1][j]
                            && Numbers[i][j] != 0) {
                        NumNearCounter++;
                    }
                    if (Numbers[3][j] == Numbers[3][j + 1]//第四行只需要判断是否与右边有重复
                            && Numbers[3][j] != 0) {
                        NumNearCounter++;
                    }
                    if (Numbers[i][3] == Numbers[i + 1][3]//第四列只需要判断与下边是否有重复
                            && Numbers[i][3] != 0) {
                        NumNearCounter++;
                    }
                }
            }
            //判断不为0的空余格式数
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (Numbers[i][j] != 0) {
                        NumCounter++;
                    }
                }
            }
            System.out.println(Counter);

            //有效移位数>0，则补充一个新的2或者4
            if (Counter > 0) {
                lb.setText("分数：" + score);
                int r1 = rand.nextInt(4);
                int c1 = rand.nextInt(4);
                while (Numbers[r1][c1] != 0) {
                    r1 = rand.nextInt(4);
                    c1 = rand.nextInt(4);
                }
                int value1 = rand.nextInt(2) * 2 + 2;
                Numbers[r1][c1] = value1;
            }

            if (isWin == true) {
                UI.paint(UI.getGraphics());
                JOptionPane.showMessageDialog(UI, "恭喜你赢了!\n您的最终得分为：" + score);
            }

            if (NumCounter == 16 && NumNearCounter == 0) {
                //移动后满格并且没有可合并的小格子，游戏结束relive:复活一次
                relive = true;
                JOptionPane.showMessageDialog(UI, "没地方可以合并咯!!"
                        + "\n很遗憾，您输了~>_<~"
                        + "\n悄悄告诉你，游戏有起死回生功能哦，不信你“退一步”试试？"
                        + "\n说不定能扭转乾坤捏 （^_~）");
            }
            UI.paint(UI.getGraphics());
        }

    }

}
