package project.zero.codescan.utils;

import java.text.DecimalFormat;

/**
 * Created by Mr HU on 2016/3/22.
 */
public class RegularUtils {
    /**
     * 使用正则表达式检查手机号码
     */
    public static boolean checkPhoneNum(String phone) {

        return phone.matches(RegularExp.REGULAR_EXPRESSION_MOBILE);
    }

    /**
     * 使用正则表达式检查密码
     */
    public static boolean checkPassWord(String pw) {

        return pw.matches(RegularExp.REGULAR_EXPRESSION_PASSWORD);
    }

    /**
     * 使用正则表达式检查用户名
     */
    public static boolean checkUsername(String name) {

        return name.matches(RegularExp.REGULAR_EXPRESSION_CONTACT);
    }

    /**
     * 银行卡号每隔四位增加一个空格
     *
     * @param input : 银行卡号,例如"6225880137706868"
     * @return
     */
    public static String formBankCard(String input) {
        String result = input.replaceAll("([\\d]{4})(?=\\d)", "$1 ");
        return result;
    }

    /**
     * 隐藏银行卡号前几位
     *
     * @param input
     * @return
     */
    public static String formBankCard2(String input) {
        String result = input.replaceAll("([\\d]{4})(?=\\d)", "**** ");
        return result;
    }

    /**
     * 隐藏银行卡号前几位
     *
     * @param input
     * @return
     */
    public static String formBankCard3(String input) {
        String result = input.replaceAll("([\\d]{4})(?=\\d)", "$1 ");
        return result;
    }

    /**
     * 格式化数字
     * 方式一:使用DecimalFormat
     */
    public static String formatDigitString(String string, int digitLength) {
//        DecimalFormat df1 = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormat df1 = new DecimalFormat("#,###.00");
        df1.setGroupingSize(digitLength);
        String result = null;
        try {
//            string = new DecimalFormat("0.00").format(Float.parseFloat(string));
            result = df1.format(Float.parseFloat(string));
        } catch (Exception e) {
            e.printStackTrace();
            return string;
        }
        return result;
    }

    /**
     * 格式化数字
     * 方式一:使用DecimalFormat
     */
    public static void formatFileSize() {
        DecimalFormat df1 = (DecimalFormat) DecimalFormat.getInstance();
        df1.setGroupingSize(3);
        String result = df1.format(1234567.45);
        System.out.println(result);
    }

    /**
     * 格式化数字
     * 方式二:使用正则表达式
     */
    public static String digit(String input) {
//        String input = "1234567.45634";
        String regx = "(?<=\\d)(\\d{4})";
//        System.out.println(input.replaceAll(regx, " $1"));
        return input.replaceAll(regx, " $1");
    }

    /**
     * 现行 16 位银联卡现行卡号开头 6 位是 622126～622925 之间的，7 到 15 位是银行自定义的，
     可能是发卡分行，发卡网点，发卡序号，第 16 位是校验码。

     16 位卡号校验位采用 Luhm 校验方法计算：

     1，将未带校验位的 15 位卡号从右依次编号 1 到 15，位于奇数位号上的数字乘以 2
     2，将奇位乘积的个十位全部相加，再加上所有偶数位上的数字
     3，将加法和加上校验位能被 10 整除。

     比如卡号：

     Java code
     .  6  2  2  5   8  8  1  4   1  4  2  0   7  4  3
     *  2     2      2     2      2     2      2     2
     --------------------------------------------------
     .  12  2  4  5  16  8  2  4   2  4  4     14  4  6

     将上面的数字加和：1+2+2+4+5+1+6+8+2+4+2+4+4+1+4+4+6 = 60

     由于 60 加上 0 才能被 10 整除，所以校验位为 0

     因此该卡号为 6225 8814 1420 7430


     如果其中一位数字换掉的话，直接导致最后校验位错误。
     */

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            throw new IllegalArgumentException("Bank card code must be number!");
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 身份证校验
     *
     * @param args
     */
    String cityJson = "{\"11\"\n" +
            "    :\"北京\",\"12\":\"天津\",\"13\":\"河北\",\"14\":\"山西\",\"15\":\"内蒙古\",\"21\":\"辽宁\",\"22\":\"吉林\",\"23\":\"黑龙江 \",\"31\":\"上海\",\"32\":\"江苏\",\"33\":\"浙江\",\"34\":\"安徽\",\"35\":\"福建\",\"36\":\"江西\",\"37\":\"山东\",\"41\":\"河南\",\"42\":\"湖北\",\"43\":\"湖南\",\"44\":\"广东\",\"45\":\"广西\",\"46\":\"海南\",\"50\":\"重庆\",\"51\":\"四川\",\"52\":\"贵州\",\"53\":\"云南\",\"54\":\"西藏 \",\"61\":\"陕西\",\"62\":\"甘肃\",\"63\":\"青海\",\"64\":\"宁夏\",\"65\":\"新疆\",\"71\":\"台湾\",\"81\":\"香港\",\"82\":\"澳门\",\"91\":\"国外\"\n" +
            "}";

//    public static boolean cidInfo(String identyId){
//        int iSum=0;
//        String info="";
//        if(!/^d{17}(d|x)$/i.test(identyId))return false;
//        sId=sId.replace(/x$/i,"a");
//        if(aCity[parseInt(sId.substr(0,2))]==null)return "Error:非法地区";
//        sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2));
//        var d=new Date(sBirthday.replace(/-/g,"/"))
//        if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))return "Error:非法生日";
//        for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11)
//        if(iSum%11!=1)return "Error:非法证号";
//        return aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女")
//    }

    public static void main(String[] args) {
        String card = "534534534534535";
        System.out.println("      card: " + card);
        System.out.println("check code: " + getBankCardCheckCode(card.substring(0, card.length() - 1)));
        System.out.println("   card id: " + card + getBankCardCheckCode(card));
        System.out.println(checkBankCard(card));
        System.out.println(formBankCard(card));
        System.out.println(formBankCard2(card));
        formatFileSize();
//        digit();
        System.out.println(formatDigitString("234545.4543", 3));
    }

    /**
     * 校验身份证
     *
     * @param ID
     * @return
     */
    public boolean checkIdentityId(String ID) {

        return Identity.checkIDCard(ID);
    }

}
