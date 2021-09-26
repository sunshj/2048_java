package Game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

//本类继承自JFrame，创建游戏窗口，只需要new本类对象
public class GameStart extends JFrame {

    private static final long serialVersionUID = 1L;

    //用于存放数据的二维数组,构成4*4网格的游戏界面数值，数组中的值就是其对应位置方格的值，0代表无值
    private int Numbers[][] = new int[4][4];

    public void init() {
        this.setTitle("2048游戏 Modified by sunshj");
        this.setLocation(450, 100);
        this.setSize(550, 500);
        this.setLayout(null);
        //背景图
        setContentPane(new JLabel(new ImageIcon("src/res/bg.png")));

        // 开始游戏按钮
        ImageIcon imgicon = new ImageIcon("src/res/start.png");
        JButton start = new JButton(imgicon);
        start.setFocusable(false);//设置此按钮不可获取焦点
        start.setBorderPainted(false);//设置此按钮有边框
        start.setFocusPainted(false);//设置不绘制边框，设置 paintFocus属性，对于要绘制的焦点状态，该属性必须为 true。paintFocus 属性的默认值为 true。一些外观没有绘制焦点状态；它们将忽略此属性
        start.setContentAreaFilled(false);//设置不绘制边框，设置 contentAreaFilled 属性。如果该属性为 true，则按钮将绘制内容区域。如果希望有一个透明的按钮，比如只是一个图标的按钮，那么应该将此属性设置为 false。不要调用 setOpaque(false)。contentAreaFilled 属性的默认值为 true。
        start.setBounds(5, 10, 120, 30);// 设置按钮的x，y坐标位置和宽度与高度
        this.add(start);
        start.setText("开始");

        //后退一步按钮
        ImageIcon backicon = new ImageIcon("src/res/back.png");
        JButton back = new JButton(backicon);
        back.setFocusable(false);
        back.setBorderPainted(false);
        back.setFocusPainted(false);
        back.setContentAreaFilled(false);
        back.setBounds(255, 10, 120, 30);// 设置按钮的x，y坐标位置和宽度与高度
        this.add(back);
        back.setText("后退");

        // 关于按钮
        ImageIcon imgicon2 = new ImageIcon("src/res/about.png");
        JButton about = new JButton(imgicon2);
        about.setFocusable(false);
        about.setBorderPainted(false);
        about.setFocusPainted(false);
        about.setContentAreaFilled(false);
        about.setBounds(152, 10, 70, 30);
        this.add(about);
        about.setText("关于");

        // 分数显示
        JLabel scoreLabel = new JLabel("分数:0");
        scoreLabel.setBounds(400, 50, 120, 90);
        scoreLabel.setBackground(Color.gray);
        scoreLabel.setOpaque(false);//不透明
        scoreLabel.setFont(new Font("阿里巴巴普惠体 Medium", Font.CENTER_BASELINE, 20));
        scoreLabel.setForeground(new Color(0xDDDDDD));

        this.add(scoreLabel);

        //游戏信息
        JLabel aboutLabel = new JLabel("<html><body><p align=\"center\">2048游戏" +
                "<br/>Modified By sunshj™<br/><br/>版本号<br/>v0.2.6</p></body></html>");
        aboutLabel.setBounds(400, 200, 120, 180);
        aboutLabel.setBackground(Color.gray);
        aboutLabel.setOpaque(false);//不透明
        aboutLabel.setFont(new Font("阿里巴巴普惠体 Medium", Font.CENTER_BASELINE, 18));
        aboutLabel.setForeground(new Color(0x3E86A0));
        Border blackline = BorderFactory.createLineBorder(Color.white);//边框为白色线框
        aboutLabel.setBorder(blackline);//添加边框
        this.add(aboutLabel);


        //音量组件
        JCheckBox isSoundBox = new JCheckBox("静音");
        isSoundBox.setBounds(400, 10, 120, 30);
        isSoundBox.setFont(new Font("幼圆", Font.CENTER_BASELINE, 18));
        isSoundBox.setFocusable(false);
        isSoundBox.setBorderPainted(false);
        isSoundBox.setFocusPainted(false);
        isSoundBox.setContentAreaFilled(false);
        isSoundBox.setForeground(new Color(0xDDDDDD));
        this.add(isSoundBox);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);//游戏界面尺寸调节
        this.setVisible(true);// 显示界面

        // 创建事件处理类
        ComponentListener cl = new ComponentListener(this, Numbers, scoreLabel, start, about, back, isSoundBox);
        start.addActionListener(cl);
        about.addActionListener(cl);
        back.addActionListener(cl);
        isSoundBox.addActionListener(cl);
        this.addKeyListener(cl);

    }

    // 重写窗体
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //设置画笔颜色
        g.setColor(new Color(0xBBADA0));
        //填充整个4*4圆角矩形区域，使用当前颜色填充指定的圆角矩形
        g.fillRoundRect(15, 110, 370, 370, 15, 15);// 大矩形框

        g.setColor(new Color(0xCDC1B4));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //填充每一个4*4小方格区域
                g.fillRoundRect(25 + i * 90, 120 + j * 90,
                        80, 80, 15, 15);// 小矩形框
            }
        }

        // 调整数字的位置并上色
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //如果小方格上数字不为0，则说明有值，进行绘制背景色与数字
                if (Numbers[j][i] != 0) {
                    int FontSize = 30;
                    int MoveX = 0, MoveY = 0;
                    switch (Numbers[j][i]) {
                        case 2:
                            g.setColor(new Color(0xeee4da));
                            FontSize = 30;
                            MoveX = 0;
                            MoveY = 0;
                            break;
                        case 4:
                            g.setColor(new Color(0xede0c8));
                            FontSize = 30;
                            MoveX = 0;
                            MoveY = 0;
                            break;
                        case 8:
                            g.setColor(new Color(0xf2b179));
                            FontSize = 30;
                            MoveX = 0;
                            MoveY = 0;
                            break;
                        case 16:
                            g.setColor(new Color(0xf59563));
                            FontSize = 29;
                            MoveX = -5;
                            MoveY = 0;
                            break;
                        case 32:
                            g.setColor(new Color(0xf67c5f));
                            FontSize = 29;
                            MoveX = -5;
                            MoveY = 0;
                            break;
                        case 64:
                            g.setColor(new Color(0xF65E3B));
                            FontSize = 29;
                            MoveX = -5;
                            MoveY = 0;
                            break;
                        case 128:
                            g.setColor(new Color(0xedcf72));
                            FontSize = 28;
                            MoveX = -10;
                            MoveY = 0;
                            break;
                        case 256:
                            g.setColor(new Color(0xedcc61));
                            FontSize = 28;
                            MoveX = -10;
                            MoveY = 0;
                            break;
                        case 512:
                            g.setColor(new Color(0xedc850));
                            FontSize = 28;
                            MoveX = -10;
                            MoveY = 0;
                            break;
                        case 1024:
                            g.setColor(new Color(0xedc53f));
                            FontSize = 27;
                            MoveX = -15;
                            MoveY = 0;
                            break;
                        case 2048:
                            g.setColor(new Color(0xedc22e));
                            FontSize = 27;
                            MoveX = -15;
                            MoveY = 0;
                            break;
                        default:
                            g.setColor(new Color(0x000000));
                            break;
                    }
                    //数字不为0的小方格覆盖原色，根据不同的值上不同的色
                    g.fillRoundRect(25 + i * 90, 120 + j * 90,
                            80, 80, 15, 15);// 小矩形框覆盖上色
                    g.setColor(new Color(0x000000));
                    g.setFont(new Font(".Keycaps", Font.PLAIN, FontSize));
                    //绘制字符串,参数分别为：要绘制的字符串，字符串绘制的x坐标，y坐标
                    g.drawString(Numbers[j][i] + "", 25 + i * 90 + 30 + MoveX,
                            120 + j * 90 + 50 + MoveY);
                }
            }
        }
    }
}
