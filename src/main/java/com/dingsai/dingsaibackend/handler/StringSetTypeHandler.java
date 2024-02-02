package com.dingsai.dingsaibackend.handler;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zzs
 * @date 2023/11/22 8:31
 */
public class StringSetTypeHandler extends BaseTypeHandler<Set<String>> {
    
    private static final Gson GSON = new Gson();
    
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Set<String> strings, JdbcType jdbcType) throws SQLException {
        String content = CollUtil.isEmpty(strings) ? null : GSON.toJson(strings);
        preparedStatement.setString(i, content);
    }

    @Override
    public Set<String> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String string = resultSet.getString(s);
        return StringUtils.isBlank(string) ? new HashSet<>() : GSON.fromJson(string, new TypeToken<Set<String>>() {}.getType());
    }

    @Override
    public Set<String> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String string = resultSet.getString(i);
        return StringUtils.isBlank(string) ? new HashSet<>() : GSON.fromJson(string, new TypeToken<Set<String>>() {}.getType());
    }

    @Override
    public Set<String> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String string = callableStatement.getString(i);
        return StringUtils.isBlank(string) ? new HashSet<>() : GSON.fromJson(string, new TypeToken<Set<String>>() {}.getType());
    }
}
