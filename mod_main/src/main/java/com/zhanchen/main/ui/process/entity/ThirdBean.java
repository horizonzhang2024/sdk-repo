package com.zhanchen.main.ui.process.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdBean {

    private String code = "200";
    private String msg = "服务器返回成功";
    private RepData repData;

    public static class RepData {
        private Map<String, List<String>> content;

        public Map<String, List<String>> getContent() {
            Map<String, List<String>> map = new HashMap<>();

            List<String> listCRH2A_A = new ArrayList<>();
            listCRH2A_A.add("快速搜索");
            listCRH2A_A.add("设备舱除尘");
            listCRH2A_A.add("内风挡检查及清洁");
            listCRH2A_A.add("蓄电池装置检查及清洁");
            listCRH2A_A.add("配电柜检查及清洁");
            listCRH2A_A.add("车厢广播影视及监控系统检查");
            listCRH2A_A.add("空调系统检查及清洁");
            listCRH2A_A.add("室内回风口、卫生间废排口滤网清洁");
            listCRH2A_A.add("换气装置及逆变器检查与清洁");
            listCRH2A_A.add("真空污物装置检查");
            listCRH2A_A.add("座椅检查");
            listCRH2A_A.add("座便器清洁");
            listCRH2A_A.add("VIP座椅检查");
            listCRH2A_A.add("卧铺设施检查及润滑");
            listCRH2A_A.add("过渡车钩检查及润滑");
            listCRH2A_A.add("自动过分相装置检测");
            listCRH2A_A.add("铅酸蓄电池装置放电检测");
            listCRH2A_A.add("接触器箱检查及清洁");
            listCRH2A_A.add("制动管路及空气软管状态检查");
            listCRH2A_A.add("受电弓监测系统检查");
            listCRH2A_A.add("应急通风装置检查及清洁");
            listCRH2A_A.add("设备舱通风口及裙板滤网等检修");
            listCRH2A_A.add("供排水装置检查及清洁");
            listCRH2A_A.add("液位显示器检查");
            listCRH2A_A.add("污物箱清洁");
            listCRH2A_A.add("水封及应急排水阀检查");
            listCRH2A_A.add("电开水炉检查及清洁");
            listCRH2A_A.add("车内卫生设施检测及清洁");
            listCRH2A_A.add("内端门及防火门检测及润滑");
            listCRH2A_A.add("客室桌椅检查及润滑");
            listCRH2A_A.add("厨房及小卖部设备滤网清理");
            listCRH2A_A.add("刮雨器检修");
            listCRH2A_A.add("前窗覆膜加热装置及车窗、外显密封性检查");
            listCRH2A_A.add("安全阀动作试验");
            listCRH2A_A.add("主空压机进气过滤器滤芯更换");
            listCRH2A_A.add("空气弹簧高度测量");
            listCRH2A_A.add("空调冷凝器滤网清洁");
            listCRH2A_A.add("牵引电机进风口滤网清洁");
            listCRH2A_A.add("换气装置逆变器滤网清洁");
            listCRH2A_A.add("换气装置滤芯清洁");
            listCRH2A_A.add("全列裙板滤网清洁");
            listCRH2A_A.add("司机室空调装置清洁");
            listCRH2A_A.add("主空压机进气过滤器滤芯清洁");
            listCRH2A_A.add("空调机组排水泵及管路清洁");
            listCRH2A_A.add("空调机组排水泵及管路清洁");
            listCRH2A_A.add("空调冷凝器、蒸发器清洁");
            listCRH2A_A.add("空调冷凝器、蒸发器清洁");
            listCRH2A_A.add("转向架清洗");
            listCRH2A_A.add("端部新风滤网清洁");
            listCRH2A_A.add("空调蒸发器滤网清洁");
            listCRH2A_A.add("空调蒸发器滤网清洁");
            listCRH2A_A.add("辅助电源装置滤网清洁");
            listCRH2A_A.add("牵引变流器滤网清洁");
            listCRH2A_A.add("牵引电机冷却风机滤网清洁");
            listCRH2A_A.add("牵引变压器清洁");
            listCRH2A_A.add("油冷却器金属过滤器网清洁");


            List<String> listCRH2A_B = new ArrayList<>();
            listCRH2A_B.add("快速搜索");
            listCRH2A_B.add("自动车钩缓冲装置及头罩开闭机构检测润滑");
            listCRH2A_B.add("转向架联轴节、轴温检测及排障装置检查");
            listCRH2A_B.add("车顶高压及附属设备检查及清洁");
            listCRH2A_B.add("牵引变压器及冷却装置检查与清洁");
            listCRH2A_B.add("牵引变流器检查及清洁");
            listCRH2A_B.add("牵引电机及冷却装置检查与清洁");
            listCRH2A_B.add("辅助电气设备检测和清洁");
            listCRH2A_B.add("制动设施检查及功能测试");
            listCRH2A_B.add("主空压机检测、润滑油取样");
            listCRH2A_B.add("辅助空气压缩机检测");
            listCRH2A_B.add("侧门装置检查、清洁及润滑");
            listCRH2A_B.add("司机室功能测试及设施检查");
            listCRH2A_B.add("齿轮箱接地装置检查");
            listCRH2A_B.add("司机室隔断门及卫生间门检查");
            listCRH2A_B.add("结构部件裂纹隐患排查");
            listCRH2A_B.add("撒砂装置功能试验");
            listCRH2A_B.add("高压设备箱检查及清洁");
            listCRH2A_B.add("接地电阻、接地继电器检查及清洁");
            listCRH2A_B.add("火灾、紧急蜂鸣器功能检查");
            listCRH2A_B.add("烟火报警系统检查及卫生间过滤棉更换");
            listCRH2A_B.add("司机室设备检查");
            listCRH2A_B.add("车体倾斜尺寸测量");
            listCRH2A_B.add("绝缘测量");
            listCRH2A_B.add("烟火报警系统客室及包间过滤棉更换");
            listCRH2A_B.add("主空压机润滑油和油过滤器更换");
            listCRH2A_B.add("转向架例行试验");
            listCRH2A_B.add("齿轮箱及附属装置检查");
            listCRH2A_B.add("一系悬挂及轴箱组成装置检查");
            listCRH2A_B.add("构架组成及其附件状态检查");
            listCRH2A_B.add("二系悬挂装置检查");
            listCRH2A_B.add("速度传感器检查");
            listCRH2A_B.add("排障撒砂装置检查及扭矩校核");
            listCRH2A_B.add("轴端接地装置碳刷高度测量");
            listCRH2A_B.add("制动盘、制动夹钳装置检查");
            listCRH2A_B.add("辅助电源装置变压器及电抗器清洁");
            listCRH2A_B.add("救援装置性能测试及耐雪制动试验");

            List<String> listCRH2A_C = new ArrayList<>();
            listCRH2A_C.add("快速搜索");
            listCRH2A_C.add("齿轮箱润滑油更换");
            listCRH2A_C.add("牵引电机轴承加注油脂");
            listCRH2A_C.add("齿轮箱润滑油更换");
            listCRH2A_C.add("空心车轴探伤");
            listCRH2A_C.add("轮对修形");
            listCRH2A_C.add("轮对修形");


            List<String> list380AL_A = new ArrayList<>();
            list380AL_A.add("快速搜索");
            list380AL_A.add("内风挡检查及清洁");
            list380AL_A.add("蓄电池装置检查及清洁");
            list380AL_A.add("配电柜检查及清洁");
            list380AL_A.add("车厢广播影视及监控系统检查");
            list380AL_A.add("空调系统检查和清洁");
            list380AL_A.add("室内回风口、卫生间废排口滤网清洁");
            list380AL_A.add("换气装置及逆变器检查与清洁");
            list380AL_A.add("座椅设施检查");
            list380AL_A.add("卧铺检查");
            list380AL_A.add("设备舱除尘");
            list380AL_A.add("车体外皮全列补洗");
            list380AL_A.add("司机室隔断门及卫生间门检查");
            list380AL_A.add("BP救援装置检查及试验");
            list380AL_A.add("齿轮箱接地装置检查");
            list380AL_A.add("座便器清洁");
            list380AL_A.add("车顶导流罩检查");
            list380AL_A.add("过渡车钩检查及润滑");
            list380AL_A.add("自动过分相装置检测");
            list380AL_A.add("接触器箱检查与清洁");
            list380AL_A.add("制动管路状态及空气软管外观检查");
            list380AL_A.add("受电弓监测系统检查");
            list380AL_A.add("应急通风装置检查及清洁");
            list380AL_A.add("设备舱通风口及裙板滤网等检修");
            list380AL_A.add("供排水装置检查及清洁");
            list380AL_A.add("真空污物装置检查及清洁");
            list380AL_A.add("液位显示器检查");
            list380AL_A.add("水封及应急排水阀检查");
            list380AL_A.add("电茶炉检查与清洁");
            list380AL_A.add("车内卫生设施检测及清洁");
            list380AL_A.add("内端门及防火门检查");
            list380AL_A.add("客室桌椅检查及润滑");
            list380AL_A.add("厨房设备滤网清理");
            list380AL_A.add("前窗覆膜加热装置及车窗、外显密封性检查");
            list380AL_A.add("烟火报警系统司机室、客室及配电柜过滤棉更换");
            list380AL_A.add("辅助电源装置变压器及电抗器清洁");
            list380AL_A.add("救援装置性能测试及耐雪制动试验");
            list380AL_A.add("安全阀动作试验");
            list380AL_A.add("辅助电源装置滤网清洁");
            list380AL_A.add("空调冷凝器滤网清洁");
            list380AL_A.add("端部新风滤网清洁");
            list380AL_A.add("司机室空调装置清洁");
            list380AL_A.add("换气装置逆变器滤网清洁");
            list380AL_A.add("牵引变流器滤网清洁");
            list380AL_A.add("牵引电机进风口滤网清洁");
            list380AL_A.add("牵引电机冷却风机滤网清洁");
            list380AL_A.add("牵引变压器清洁");
            list380AL_A.add("油冷却器金属过滤器网清洁");
            list380AL_A.add("全列裙板滤网清洁");
            list380AL_A.add("空调机组排水泵及管路清洁");
            list380AL_A.add("空调机组冷凝器、蒸发器清洁");
            list380AL_A.add("主空压机滤芯清洁");
            list380AL_A.add("转向架清洗");
            list380AL_A.add("空调蒸发器滤网清洁");

            List<String> list380AL_B = new ArrayList<>();
            list380AL_B.add("快速搜索");
            list380AL_B.add("前端车钩及头罩开机构检测润滑");
            list380AL_B.add("转向架联轴节、轴温检测及排障装置检查");
            list380AL_B.add("车顶高压及附属设备检查及清洁");
            list380AL_B.add("牵引变压器及冷却装置检查与清洁");
            list380AL_B.add("牵引变流器检查及清洁");
            list380AL_B.add("牵引电机及冷却装置检查与清洁");
            list380AL_B.add("辅助电气设备检测和清洁");
            list380AL_B.add("制动设施检查及功能测试");
            list380AL_B.add("主空压机检测、清洁及润滑油取样");
            list380AL_B.add("辅助空气压缩机检测");
            list380AL_B.add("火灾、紧急蜂鸣器功能检查");
            list380AL_B.add("侧门装置检查、清洁及润滑");
            list380AL_B.add("司机室功能测试及设施检查");
            list380AL_B.add("撒砂装置功能试验");
            list380AL_B.add("高压设备箱检查及清洁");
            list380AL_B.add("接地电阻及接地继电器检查及清洁");
            list380AL_B.add("烟火报警系统检查及卫生间过滤棉更换");
            list380AL_B.add("司机室设备检查");
            list380AL_B.add("刮雨器检修");
            list380AL_B.add("车体倾斜尺寸测量");
            list380AL_B.add("绝缘测量");
            list380AL_B.add("主空压机润滑油及滤芯更换");
            list380AL_B.add("转向架例行试验");
            list380AL_B.add("齿轮箱及附属装置检查");
            list380AL_B.add("一系悬挂及轴箱组成装置检查");
            list380AL_B.add("构架组成及其附件状态检查");
            list380AL_B.add("二系悬挂装置检查");
            list380AL_B.add("速度传感器检查");
            list380AL_B.add("排障撒砂装置检查及扭矩校核");
            list380AL_B.add("轴端接地装置碳刷高度测量");
            list380AL_B.add("制动盘、制动夹钳装置检查");
            list380AL_B.add("ATP电源断路器更换");
            list380AL_B.add("主空压机滤芯更换");
            list380AL_B.add("主空压机管道过滤器检修");
            list380AL_B.add("主空压机节流喷嘴检修");
            list380AL_B.add("空气弹簧高度测量");
            list380AL_B.add("动车组玻璃及结构部件检查");

            List<String> list380AL_C = new ArrayList<>();
            list380AL_C.add("快速搜索");
            list380AL_C.add("牵引电机轴承加注油脂");
            list380AL_C.add("齿轮箱润滑油更换(福伊特)");
            list380AL_C.add("齿轮箱润滑油更换(东洋电机)");
            list380AL_C.add("空心车轴探伤");
            list380AL_C.add("轮辋轮辐超声波探伤");
            list380AL_C.add("轮对修形");
            list380AL_C.add("关键项目深度检查");


            List<String> list400AF_A = new ArrayList<>();
            list400AF_A.add("快速搜索");
            list400AF_A.add("头车自动电连接器检查");
            list400AF_A.add("外风挡检查");
            list400AF_A.add("内风挡检查与清洁");
            list400AF_A.add("牵引电机通风装置检查及清洁");
            list400AF_A.add("充电机检查及清洁（四方所-时代电气）");
            list400AF_A.add("交流电气柜检查及清洁");
            list400AF_A.add("直流电气柜检查及清洁");
            list400AF_A.add("控制柜、车下控制箱检查及清洁");
            list400AF_A.add("厨房配电柜检查及清洁");
            list400AF_A.add("主空压机油位及真空指示器检查");
            list400AF_A.add("视频监控系统检查");
            list400AF_A.add("旅客信息及娱乐系统检查");
            list400AF_A.add("无线上网系统检查");
            list400AF_A.add("车内压力保护装置检查");
            list400AF_A.add("应急排水阀清洁");
            list400AF_A.add("污物箱检查及清洁");
            list400AF_A.add("供排水装置检查及清洁");
            list400AF_A.add("便器冲洗喷嘴清洁");
            list400AF_A.add("婴儿护理台检查");
            list400AF_A.add("卫生间过滤盒清洁");
            list400AF_A.add("内装检查");
            list400AF_A.add("客室一、二等座椅检查");
            list400AF_A.add("VIP座椅检查");
            list400AF_A.add("厨房系统检查");
            list400AF_A.add("厨房电气检查");
            list400AF_A.add("司机室电气边柜检查及清洁");
            list400AF_A.add("刮雨器安装螺栓检查");
            list400AF_A.add("风笛、电笛检查");
            list400AF_A.add("司机座椅清洁及功能检查");
            list400AF_A.add("司机登乘门检查");
            list400AF_A.add("撒砂功能测试");
            list400AF_A.add("接触器箱检查及清洁");
            list400AF_A.add("电开水炉检查及清洁");
            list400AF_A.add("便器软管、排污管接头及盥洗设备检查");
            list400AF_A.add("司机座椅支架系统润滑");
            list400AF_A.add("撒砂装置检查及功能测试");
            list400AF_A.add("外接电源连接器箱检查");
            list400AF_A.add("厨房系统、厨房电气检查");
            list400AF_A.add("动车组座便器清洁");
            list400AF_A.add("电子标签检查");
            list400AF_A.add("撒砂功能测试");
            list400AF_A.add("盥洗室过滤盒清洁");
            list400AF_A.add("客室空调混合风滤网清洗");
            list400AF_A.add("车下设备舱裙板滤网清扫");
            list400AF_A.add("牵引变流器滤网检查");

            List<String> list400AF_B = new ArrayList<>();
            list400AF_B.add("快速搜索");
            list400AF_B.add("车体倾斜尺寸测量");
            list400AF_B.add("前头排障装置检查及安装紧固");
            list400AF_B.add("头罩开闭机构装置检查及润滑");
            list400AF_B.add("高压设备箱检查及清洁");
            list400AF_B.add("车顶避雷器及半刚性终端检查");
            list400AF_B.add("自动过分相装置检查");
            list400AF_B.add("牵引变流器冷却液检查与补充（时代电气）");
            list400AF_B.add("主供风单元检查");
            list400AF_B.add("主供风单元微油过滤器排油");
            list400AF_B.add("风缸模块装置检查");
            list400AF_B.add("前端气动部件检查");
            list400AF_B.add("制动供风管路状态及空气软管外观检查");
            list400AF_B.add("安全阀检查");
            list400AF_B.add("辅助供风单元检查");
            list400AF_B.add("制动控制装置检查");
            list400AF_B.add("BP救援转换装置检查");
            list400AF_B.add("WTD主机检查（时代电气、纵横机电）");
            list400AF_B.add("烟火报警系统检查（亚通达）");
            list400AF_B.add("客室空调系统检查");
            list400AF_B.add("司机室空调检查");
            list400AF_B.add("废排装置检查");
            list400AF_B.add("操纵台设备检查");
            list400AF_B.add("过渡车钩检查及润滑");
            list400AF_B.add("车顶高压连接电缆护套检查");
            list400AF_B.add("高压绝缘设备箱设备检查");
            list400AF_B.add("自动过分相装置检测");
            list400AF_B.add("牵引变压器检查及油样检测");
            list400AF_B.add("牵引变流器辅助变压器、电抗器检查");
            list400AF_B.add("辅助回路接线箱检查");
            list400AF_B.add("主供风单元冷却器清洁及空气滤清器滤芯更换");
            list400AF_B.add("辅助供风单元性能检查");
            list400AF_B.add("制动功能检查");
            list400AF_B.add("BP救援转换装置功能测试");
            list400AF_B.add("整备模式试验");
            list400AF_B.add("前端车钩缓冲装置检查及润滑");
            list400AF_B.add("中间车钩缓冲装置检查及润滑");
            list400AF_B.add("蓄电池检查（亚通达）");
            list400AF_B.add("塞拉门检测与清洁");
            list400AF_B.add("前窗电热功能检查");
            list400AF_B.add("抗蛇行油压减振器拆装及检测");
            list400AF_B.add("垂向、横向及车端油压减振器状态检查");
            list400AF_B.add("抗侧滚扭杆装置注脂");
            list400AF_B.add("充电机检查及清洁");
            list400AF_B.add("主供风单元功能测试及润滑油、过滤器更换");
            list400AF_B.add("空调系统检查及清洁");
            list400AF_B.add("残疾人卫生间门检测及润滑");
            list400AF_B.add("内外端门检测及手动门润滑");
            list400AF_B.add("空气弹簧高度测量");
            list400AF_B.add("牵引变压器检查及清洁");
            list400AF_B.add("动车组玻璃及结构部件检查");
            list400AF_B.add("轴温实时检测系统设备检测");
            list400AF_B.add("安全监控装置箱检查及清洁");
            list400AF_B.add("中央控制装置箱检查及清洁");
            list400AF_B.add("电压表、风压表功能检查");

            List<String> list400AF_C = new ArrayList<>();
            list400AF_C.add("快速搜索");
            list400AF_C.add("轮对尺寸人工测量");
            list400AF_C.add("联轴节检查");
            list400AF_C.add("受电弓检查");
            list400AF_C.add("关键项目深度检查");
            list400AF_C.add("牵引电机轴承润滑");
            list400AF_C.add("齿轮箱清洗及润滑油更换");
            list400AF_C.add("制动盘、制动夹钳装置检查");
            list400AF_C.add("前端车钩缓冲装置检查及润滑");
            list400AF_C.add("中间车钩缓冲装置检查");
            list400AF_C.add("齿轮箱及吊挂装置状态检查");
            list400AF_C.add("齿轮箱接地装置弹簧压力测试及接地视窗检查");
            list400AF_C.add("轴箱弹簧组成状态检查");
            list400AF_C.add("构架组成及其附件状态检查");
            list400AF_C.add("空气弹簧组成状态检查");
            list400AF_C.add("空心车轴探伤及轴端接地装置检查");
            list400AF_C.add("轮对修形");
            list400AF_C.add("轮辋轮辐超声波探伤");


            List<String> list400BF_A = new ArrayList<>();
            list400BF_A.add("快速搜索");
            list400BF_A.add("裙板及设备舱检查");
            list400BF_A.add("车端连接装置检查");
            list400BF_A.add("自动过分相装置检查");
            list400BF_A.add("充电机检查及清洁");
            list400BF_A.add("储风缸检查及排水");
            list400BF_A.add("辅助供风单元检查");
            list400BF_A.add("车内电气设备柜检查及清洁");
            list400BF_A.add("WTD主机检查");
            list400BF_A.add("视频监控系统检查");
            list400BF_A.add("旅客信息及娱乐系统检查");
            list400BF_A.add("空调系统检查");
            list400BF_A.add("车内压力保护装置检查");
            list400BF_A.add("给排水系统检查");
            list400BF_A.add("客室设施检查");
            list400BF_A.add("司机室设备检查");
            list400BF_A.add("座椅检查及润滑");
            list400BF_A.add("供风和制动系统的气密性测试");
            list400BF_A.add("电子标签检查");
            list400BF_A.add("电开水炉检查及清洁");
            list400BF_A.add("厨房系统检查");
            list400BF_A.add("自动整备试验");
            list400BF_A.add("表类功能检测");
            list400BF_A.add("车体排水检查");
            list400BF_A.add("过渡车钩检查及润滑");
            list400BF_A.add("自动过分相装置检查");
            list400BF_A.add("充电机检查");
            list400BF_A.add("空调系统检查");
            list400BF_A.add("端门检测");
            list400BF_A.add("压力开关(L05)测试");
            list400BF_A.add("卫生间设施检查及清洁");
            list400BF_A.add("上部设施检查");
            list400BF_A.add("动车组座便器清洁");
            list400BF_A.add("牵引变压器防护网、过滤器清洁");
            list400BF_A.add("牵引变流器防护网、过滤器清洁");
            list400BF_A.add("全列裙板滤网清洁");
            list400BF_A.add("牵引电机冷却风机滤网清洁");
            list400BF_A.add("全列空调滤网清洁");
            list400BF_A.add("牵引变压器冷却器清洁");
            list400BF_A.add("牵引变流器冷却器清洁");
            list400BF_A.add("转向架清洗");


            List<String> list400BF_B = new ArrayList<>();
            list400BF_B.add("快速搜索");
            list400BF_B.add("前端部件及车钩检查");
            list400BF_B.add("转向架部件检查");
            list400BF_B.add("轴温报警系统检查");
            list400BF_B.add("转向架失稳测试装置(BIDS)检查");
            list400BF_B.add("撒砂装置及电子砂位显示器功能检查");
            list400BF_B.add("受电弓检查");
            list400BF_B.add("牵引变压器及冷却装置检查");
            list400BF_B.add("牵引辅助变流器及冷却装置检查");
            list400BF_B.add("牵引电机通风装置检查及清洁");
            list400BF_B.add("主供风装置检查");
            list400BF_B.add("制动装置检查");
            list400BF_B.add("BP救援转换装置检查");
            list400BF_B.add("烟火报警系统检查");
            list400BF_B.add("塞拉门检测及清洁润滑");
            list400BF_B.add("登乘门检查及清洁润滑（康尼）");
            list400BF_B.add("KWD联轴节波纹管灰尘清理");
            list400BF_B.add("高压绝缘检测箱检查");
            list400BF_B.add("牵引辅助变流器检查维护");
            list400BF_B.add("蓄电池及蓄电池箱检查");
            list400BF_B.add("主供风单元维护");
            list400BF_B.add("辅助空压机性能检查及更换空气滤清器滤芯");
            list400BF_B.add("车辆间跨接电缆检查");
            list400BF_B.add("牵引辅助变流器深度检查");
            list400BF_B.add("撒砂功能测试");
            list400BF_B.add("蓄电池及蓄电池箱检查维护");
            list400BF_B.add("主供风单元维护及功能测试");
            list400BF_B.add("制动功能测试");
            list400BF_B.add("BP救援转换装置功能测试");

            List<String> list400BF_C = new ArrayList<>();
            list400BF_C.add("快速搜索");
            list400BF_C.add("牵引电机轴承润滑");
            list400BF_C.add("齿轮箱检查与换油（ZF）");
            list400BF_C.add("齿轮箱检查与换油（戚墅堰）");
            list400BF_C.add("牵引变压器油样检测");
            list400BF_C.add("空心车轴超声波探伤");
            list400BF_C.add("车轮超声波探伤");
            list400BF_C.add("车轮修形");
            list400BF_C.add("关键部件深度检查");


            List<String> listGeneral = new ArrayList<>();
            listGeneral.add("车顶");
            listGeneral.add("车内");
            listGeneral.add("车下");


            List<String> listHJFC = new ArrayList<>();
            listHJFC.add("自定义");

            List<String> listZDPC = new ArrayList<>();
            listZDPC.add("自定义");

            List<String> listJSGZ = new ArrayList<>();
            listJSGZ.add("自定义");

            List<String> listLXZY = new ArrayList<>();
            listLXZY.add("自定义");

            map.put("CRH2A&A类", listCRH2A_A);
            map.put("CRH2A&B类", listCRH2A_B);
            map.put("CRH2A&C类", listCRH2A_C);
            map.put("CRH2A&通用", listGeneral);
            map.put("CRH2A&互检复查", listHJFC);
            map.put("CRH2A&重点普查", listZDPC);
            map.put("CRH2A&技术改造", listJSGZ);
            map.put("CRH2A&临修作业", listLXZY);

            map.put("CRH-380AL&A类", list380AL_A);
            map.put("CRH-380AL&B类", list380AL_B);
            map.put("CRH-380AL&C类", list380AL_C);
            map.put("CRH-380AL&通用", listGeneral);
            map.put("CRH-380AL&互检复查", listHJFC);
            map.put("CRH-380AL&重点普查", listZDPC);
            map.put("CRH-380AL&技术改造", listJSGZ);
            map.put("CRH-380AL&临修作业", listLXZY);

            map.put("CR400AF&A类", list400AF_A);
            map.put("CR400AF&B类", list400AF_B);
            map.put("CR400AF&C类", list400AF_C);
            map.put("CR400AF&通用", listGeneral);
            map.put("CR400AF&互检复查", listHJFC);
            map.put("CR400AF&重点普查", listZDPC);
            map.put("CR400AF&技术改造", listJSGZ);
            map.put("CR400AF&临修作业", listLXZY);

            map.put("CR400BF&A类", list400BF_A);
            map.put("CR400BF&B类", list400BF_B);
            map.put("CR400BF&C类", list400BF_C);
            map.put("CR400BF&通用", listGeneral);
            map.put("CR400BF&互检复查", listHJFC);
            map.put("CR400BF&重点普查", listZDPC);
            map.put("CR400BF&技术改造", listJSGZ);
            map.put("CR400BF&临修作业", listLXZY);

            return map;
        }

        public void setContent(Map<String, List<String>> content) {
            this.content = content;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RepData getRepData() {
        return repData = new RepData();
    }

    public void setRepData(RepData repData) {
        this.repData = repData;
    }
}
