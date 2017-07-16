package com.wit.fgj;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wit.qix.client.support.QicClient;
import com.wit.qix.prot.dev.QixDeveloper;
import com.wit.qix.prot.dev.command.FindDeveloperCommand;

/**
 * 开发测试用的控制器，模拟一个应用。
 *
 * @author yw
 *
 */
@Controller
public class FgjDevController {

    @Autowired private QicClient qicClient;
    @Autowired private ServerProperties serverProperties;

    @GetMapping("/dev")
    public ModelAndView index() throws Exception {
        QixDeveloper[] bound = qicClient.getQixDeveloperService().findAllBoundDevelopers();

        String hint = "";
        int proxyPort = getProxyPort();
        if (proxyPort > 0) {
            if (!hasBinding(bound, proxyPort)) {
                hint = "您当前尚未绑定" + proxyPort + "端口！";
            }
        }

        QixDeveloper developer = qicClient.getQixDeveloperService().findDeveloper(FindDeveloperCommand.builder()
                ._port__(proxyPort)
                ._build_());

        ModelAndView mav = new ModelAndView("dev/index");
        mav.addObject("now", new Date());
        mav.addObject("developer", developer);
        mav.addObject("proxyPort", proxyPort);
        mav.addObject("serverPort", serverProperties.getPort());
        mav.addObject("qisRootUrl", qicClient.getQisServer());
        mav.addObject("qisHost", qicClient.getQisServer().getHost());
        mav.addObject("userPortBindings", bound);
        mav.addObject("hint", hint);
        return mav;
    }

    private boolean hasBinding(QixDeveloper[] developers, int proxyPort) {
        for (QixDeveloper binding : developers) {
            if (binding.getBoundPort() == proxyPort) {
                return true;
            }
        }
        return false;
    }

    private int getProxyPort() {
        Pattern pattern = Pattern.compile("[a-z]+(\\d+(\\.\\d+)?$)");
        Matcher matcher = pattern.matcher(serverProperties.getContextPath());
        if (matcher.find()) {
            String port = matcher.group(1);
            return Integer.valueOf(port);
        }
        return 0;
    }

}
