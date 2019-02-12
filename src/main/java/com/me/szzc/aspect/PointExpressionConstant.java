package com.me.szzc.aspect;

/**
 * PointExpressionConstant
 *
 * @author scofieldcai
 * @message Created by scofieldcai-dev on 15/9/24.
 */
public interface PointExpressionConstant {

    /**
     * The controller _ all _ method.
     */
    String CONTROLLER_ALL_METHOD = "execution(* com.me.szzc.controller.*.*(..))";

    /**
     * The global _ exception _ all _ method.
     */
    String GLOBAL_EXCEPTION_ALL_METHOD = "@annotation(org.springframework.web.bind.annotation.ExceptionHandler)";

    /**
     * The global _ sys _ log.
     */
    String GLOBAL_SYS_LOG = "@annotation(com.me.szzc.aspect.SysLog)";

    /**
     * The global _ responsebody.
     */
    String GLOBAL_RESPONSEBODY = "@annotation(org.springframework.web.bind.annotation.ResponseBody)";
}
