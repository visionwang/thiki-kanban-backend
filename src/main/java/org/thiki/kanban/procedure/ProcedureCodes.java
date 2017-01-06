package org.thiki.kanban.procedure;

import org.thiki.kanban.foundation.application.DomainOrder;

/**
 * Created by xubt on 8/8/16.
 */
public enum ProcedureCodes {
    TITLE_IS_ALREADY_EXISTS("001", "该名称已经被使用,请使用其它名称。"),
    IS_NOT_CURRENT_TEAM_MEMBER("002", "当前团队并非团队成员。"),
    PROCEDURE_IS_NOT_EXIST("003", "你正在操作的工序不存在。"),
    DONE_PROCEDURE_IS_ALREADY_EXIST("004", "完成工序已经存在。"),
    PROCEDURE_TYPE_IS_NOT_IN_SPRINT("005", "当前工序非迭代中的工序，不可以设置完成列。"),
    NOT_ALLOW_SET_PROCEDURE_TO_ARCHIVE_DIRECTLY("006", "不允许直接将工序设置为归档状态。");

    public static final String titleIsRequired = "工序名称不能为空。";
    public static final String titleIsInvalid = "工序名称长度超限,请保持在30个字符以内。";
    public static final String descriptionIsInvalid = "工序描述长度超限,请保持在100个字符以内。";
    public static final Integer PROCEDURE_DONE = 2;
    public static final Integer PROCEDURE_TYPE_IN_PLAN = 1;
    public static final Integer PROCEDURE_TYPE_ARCHIVE = 9;
    private String code;
    private String message;

    ProcedureCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return Integer.parseInt(DomainOrder.PROCEDURE + "" + code);
    }

    public String message() {
        return message;
    }
}
