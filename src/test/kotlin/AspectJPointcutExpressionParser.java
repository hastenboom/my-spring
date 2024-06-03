import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Student
 */
public class AspectJPointcutExpressionParser
{

    public static void main(String[] args)
    {
        AspectJPointcutExpressionParser parser = new AspectJPointcutExpressionParser();
        String expression = "execution(* com.example.service..*(String,String))";
        String expression2 = "execution(String com.example.service.UserService.login(..))";
        parser.parsePointcutExpression(expression);
        parser.parsePointcutExpression(expression2);

    }

    //String com.example.service.UserService.login(String,String)
    //* com.example.service..login(String, String)，service包下所有带login(String, String)的方法
    //* com.example.service..*(..)，service“包下所有”带“任意参数”的“所有方法”
    public Matcher isExpressionTypeSupported(String expression)
    {
        String pattern = "^execution\\((.*)\\)";
        Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(expression);

        return m;
    }


    String returnType = null;
    String[] argsList = null;
    String methodName = null;
    String packageAndClassName = null;
    String className = null;
    String packageName = null;


    //? 考虑这里String的使用方式是否得当，是否应该用builder或buffer
    public void parsePointcutExpression(String expression)
    {
        // 正则表达式匹配execution模式的切点表达式

        Matcher matcher = isExpressionTypeSupported(expression);
        String detail = null;
        if (matcher.find())
        {
            detail = matcher.group(1);
            String[] split = detail.split("\\s");

            //returnType
            returnType = split[0];
            System.out.println("ReturnType: " + returnType);

            String rest1 = split[1];// package + class + method +args

            int leftBraceIndex = rest1.indexOf("(");//set package+class+method and args into two parts;
            int rightBraceIndex = rest1.indexOf(")");

            String unhandledArgs = rest1.substring(leftBraceIndex + 1, rightBraceIndex);
            if (unhandledArgs.contains(".."))
            {
                argsList = new String[]{unhandledArgs};
            }
            else
            {
                argsList = unhandledArgs.split(",");
            }
            System.out.println("argsList: " + Arrays.toString(argsList));


            //methodName
            //* com.example.service..*(..)，service“包下所有”带“任意参数”的“所有方法”
            //                      i here
            //* com.example.service..login(String, String)，service包下所有带login(String, String)的方法
            int i = rest1.lastIndexOf(".", leftBraceIndex - 1);
            if (rest1.charAt(leftBraceIndex - 1) == '*')
            {
                methodName = "*";
            }
            else
            {
                methodName = rest1.substring(i + 1, leftBraceIndex);
            }
            System.out.println("MethodName: " + methodName);


            //packageAndClassName
            String unhandledPackageAndClassName = rest1.substring(0, i);
            packageAndClassName = unhandledPackageAndClassName;
            System.out.println("packageAndClassName: " + packageAndClassName);
            if (unhandledPackageAndClassName.endsWith("."))
            {
                // it indicates any class in the package
                className = ".";
            }
            else
            {
                className = unhandledPackageAndClassName.substring(unhandledPackageAndClassName.lastIndexOf(".") + 1);
            }
            packageName= unhandledPackageAndClassName.substring(0, unhandledPackageAndClassName.lastIndexOf("."));

//            System.out.println("packageAndClassName: " + packageAndClassName);
            System.out.println("packageName: " + packageName);
            System.out.println("className: " + className);


            System.out.println();

        }
    }


}
