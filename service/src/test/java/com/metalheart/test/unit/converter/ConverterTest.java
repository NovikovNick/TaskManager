package com.metalheart.test.unit.converter;


import com.metalheart.converter.TaskToTaskJpaConverter;
import com.metalheart.model.Task;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

public class ConverterTest {

    @Test
    public void testTaskModelToTaskConverter() {

        // arrange
        var converter = new TaskToTaskJpaConverter();

        var src = new Task();
        src.setId(RandomUtils.nextInt());
        src.setTitle(RandomStringUtils.random(5));

        // act
        var dst = converter.convert(src);

        // assert
        Assert.assertEquals(src.getId(), dst.getId());
        Assert.assertEquals(src.getTitle(), dst.getTitle());
    }
}
