package com.migu.cn.utils.type;

import com.migu.cn.constant.Constant;
import com.migu.cn.utils.ServiceException;
import com.migu.cn.webapi.data.BaseLineRequest;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by le on 2017/6/28.
 */
public class RequestValidataUtil {

    public static boolean ValidataBaseLineRequest(BaseLineRequest request) throws IllegalAccessException {
        Field[] fields = BaseLineRequest.class.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if(Constant.LogicVariable.NO.equals(column.isNull())){
                if (field.getType() == String.class && StringUtils.isEmpty((String) (field.get(request)))) {
                    throw new ServiceException(column.name() + "不能为空！");
                } else if (null == field.get(request)) {
                    throw new ServiceException(column.name() + "不能为空！");
                }
            }
        }
        return false;
    }
}
