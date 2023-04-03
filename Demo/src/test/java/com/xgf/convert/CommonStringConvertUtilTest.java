package com.xgf.convert;

import com.xgf.constant.enumclass.LetterTypeEnum;
import org.junit.Test;


/**
 * @author strive_day
 * @create 2023-04-03 15:53
 * @description
 */

public class CommonStringConvertUtilTest {


    @Test
    public void test_humpToFormat() {
        System.out.println(CommonStringConvertUtil.humpToFormat("aaBbCcDEFLetter", "_", LetterTypeEnum.FIRST_LETTER_UPPERCASE));
        System.out.println(CommonStringConvertUtil.humpToFormat("aaBbCcDEFLetter", "_", LetterTypeEnum.ALL_LETTER_UPPERCASE));
        System.out.println(CommonStringConvertUtil.humpToFormat("aaBbCcDEFLetter", "_", LetterTypeEnum.ALL_LETTER_LOWERCASE));
    }

    @Test
    public void formatToHump() {
        System.out.println(CommonStringConvertUtil.formatToHump("aa_bb_cc_d_e_f_letter", "_"));
    }

    @Test
    public void humpToUnderLine() {
        System.out.println(CommonStringConvertUtil.humpToUnderLine("aa_bb_cc_d_e_f_letter"));
    }

}
