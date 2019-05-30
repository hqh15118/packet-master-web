--
-- Created by IntelliJ IDEA.
-- User: hongqianhui
-- Date: 2019-05-28
-- Time: 21:30
-- To change this template use File | Settings | File Templates.
--

local transcoder =  {
    ['name'] = 'hongqianhui'
}
-- 无返回对象的调用情况
function helloWithoutTranscoder()
    print 'hello, sky -- no transcoder '
end
-- 无参
function transcoder.hello()
    print 'hello'
    return 'hello, sky'
end
-- 参数为string数据
function transcoder.test(str)
    print('data from java is:'..str)
    return 'the parameter is '..str
end
-- 返回一个lua对象
function transcoder.getInfo()
    return {
        ['userId'] = '9999',
        ['services'] =
        {{
            'eat',
            'drink'
        }, {
            'eat2',
            'drink2'
        }}
    }
end
--[[
   infoObj-json示例:
        {
            'userId': '1111',
            'services': [{
               '0' : 'eat-test',
               '1' : 'drink-test'
            }]
      }
--]]
-- 传入一个lua对象
function transcoder.readInfo(infoObj)
    return infoObj.userId
end

function do_map(mapObj)
    mapObj:put('k','v')
end

return transcoder
