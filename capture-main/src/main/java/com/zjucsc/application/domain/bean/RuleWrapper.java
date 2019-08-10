package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class RuleWrapper {
    private boolean empty;
    private List<Rule> removedRules;
}
