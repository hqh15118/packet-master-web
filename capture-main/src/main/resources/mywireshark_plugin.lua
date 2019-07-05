-- @brief A simple post-dissector, just append string to info column
-- @author hqh
-- https://wiki.wireshark.org/Lua/Examples?action=AttachFile&do=get&target=dissector.lua 《参考》


local ext = Proto("ext","Dummy proto to edit info column") --param1 协议名字 param2 说明
local pf_trasaction_id = ProtoField.new   ("ext_raw_data", "custom_ext_raw_data", ftypes.STRING) --new一个自定义参数
ext.fields = { pf_trasaction_id}

-- the dissector function callback
function ext.dissector(tvb,pinfo,tree)
    -- local pktlen = tvb:len()
    -- pinfo.cols.info:append(" --length"..pktlen)   -- 在info后面添加数据
    local subtree = tree:add(ext,"ext");    -- 在根树后面加一个协议ext，返回该协议下的根节点
    subtree:append_text("raw_data")         -- 在自定义的子树上添加说明
    --subtree:add(pf_trasaction_id, tvb:range(0, pktlen),"ORZ")    -- 在子树上添加自定义的节点，并添加该节点的数据
    local ba = tvb:bytes();
    subtree:add(pf_trasaction_id, ba:tohex())
end

-- register our new dummy protocol for post-dissection
register_postdissector(ext)

