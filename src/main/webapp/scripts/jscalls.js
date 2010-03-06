/* 
 *
 * The MIT License
 * Copyright (c) 2010 Pere Villega Montenegro
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 *
 * JQuery_1.3.2-based methods
 */

/* TODO: the javascript code should be generated from Lift to reuse the Country/Terrain classes */

/* Creates the rows to input the armies */
var corpattnum = 1;
function addCorpAtt() {
    var line = "<tr id='att_corp_"+corpattnum+"'>";
    line += "<td><input type='text' name='att_name_"+corpattnum+"' value='Corp "+corpattnum+"' maxlength='8' class='corpname' /></td>";
    line += "<td>"+select_country("att_country_"+corpattnum)+"</td>";
    line +="<td><input type='text' name='att_g_"+corpattnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='att_a_"+corpattnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='att_i_"+corpattnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='att_m_"+corpattnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='att_c_"+corpattnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='att_ck_"+corpattnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='att_gr_"+corpattnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='att_fi_"+corpattnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='att_fc_"+corpattnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='radio' name='att_section_"+corpattnum+"' value='main' checked='checked' /></td>";
    line +="<td><input type='radio' name='att_section_"+corpattnum+"' value='outflank'  /></td>";
    line +="<td><input type='radio' name='att_section_"+corpattnum+"' value='reinforce_1' /></td>";
    line +="<td><input type='radio' name='att_section_"+corpattnum+"' value='reinforce_2' /></td>";
    line +="<td><input type='radio' name='att_section_"+corpattnum+"' value='reinforce_3' /></td>";
    line +="<td><input type='radio' name='att_section_"+corpattnum+"' value='reinforce_4' /></td>";
    line +="<td><input type='radio' name='att_section_"+corpattnum+"' value='reinforce_5' /></td>";
    line +="<td><input type='button' onclick=\"removeEntry('att_corp_"+corpattnum+"');\" value='-' /></td>";
    line +="</tr>";
    $("input[name='att_corpnum']").val(corpattnum);
    corpattnum++;
    $("#corpsatt").append(line);
    $('input[type="text"]').change( function () {
        if($(this).val().trim() == "") {
            $(this).val(0)
        }
    });
}

var corpdefnum = 1;
function addCorpDef() {
    var line = "<tr id='df_corp_"+corpdefnum+"'>";
    line += "<td><input type='text' name='df_name_"+corpdefnum+"' value='Corp "+corpdefnum+"' maxlength='8' class='corpname' /></td>";
    line += "<td>"+select_country("df_country_"+corpdefnum)+"</td>";
    line +="<td><input type='text' name='df_g_"+corpdefnum+"' value='0' maxlength='3' class='corpunit'  /></td>";
    line +="<td><input type='text' name='df_a_"+corpdefnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='df_i_"+corpdefnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='df_m_"+corpdefnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='df_c_"+corpdefnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='df_ck_"+corpdefnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='df_gr_"+corpdefnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='df_fi_"+corpdefnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='text' name='df_fc_"+corpdefnum+"' value='0' maxlength='3' class='corpunit' /></td>";
    line +="<td><input type='radio' name='df_section_"+corpdefnum+"' value='main' checked='checked' /></td>";
    line +="<td><input type='radio' name='df_section_"+corpdefnum+"' value='outflank'  /></td>";
    line +="<td><input type='radio' name='df_section_"+corpdefnum+"' value='reinforce_1' /></td>";
    line +="<td><input type='radio' name='df_section_"+corpdefnum+"' value='reinforce_2' /></td>";
    line +="<td><input type='radio' name='df_section_"+corpdefnum+"' value='reinforce_3' /></td>";
    line +="<td><input type='radio' name='df_section_"+corpdefnum+"' value='reinforce_4' /></td>";
    line +="<td><input type='radio' name='df_section_"+corpdefnum+"' value='reinforce_5' /></td>";
    line +="<td><input type='button' onclick=\"removeEntry('df_corp_"+corpdefnum+"');\" value='-' /></td>";
    line +="</tr>";
    $("input[name='df_corpnum']").val(corpdefnum);
    corpdefnum++;
    $("#corpsdef").append(line);
    $('input[type="text"]').change( function () {
        if($(this).val().trim() == "") {
            $(this).val(0)
        }
    });
}


/* Creates rows to add commanders */
var commattnum = 1;
function addComAtt() {
    var line = "<tr id='att_com_"+commattnum+"'>"
    line += "<td><input type='text' name='att_com_name_"+commattnum+"' value='Commander "+commattnum+"' maxlength='20' class='commandername' /></td>";
    line += "<td>"+select_country("att_com_country_"+commattnum)+"</td>";
    line +="<td>"+select_attribute("att_com_s_"+commattnum)+"</td>";
    line +="<td>"+select_attribute("att_com_t_"+commattnum)+"</td>";
    line +="<td>"+select_attribute("att_com_mt_"+commattnum)+"</td>";
    line += "<td>"+select_veteran("att_com_v_"+commattnum)+"</td>";
    line +="<td><input type='radio' name='att_com_section_"+commattnum+"' value='main' checked='checked' /></td>";    
    line +="<td><input type='radio' name='att_com_section_"+commattnum+"' value='reinforce_1' /></td>";
    line +="<td><input type='radio' name='att_com_section_"+commattnum+"' value='reinforce_2' /></td>";
    line +="<td><input type='radio' name='att_com_section_"+commattnum+"' value='reinforce_3' /></td>";
    line +="<td><input type='radio' name='att_com_section_"+commattnum+"' value='reinforce_4' /></td>";
    line +="<td><input type='radio' name='att_com_section_"+commattnum+"' value='reinforce_5' /></td>";
    line +="<td><input type='button' onclick=\"removeEntry('att_com_"+commattnum+"');\" value='-' /></td>";
    line += "</tr>";
    $("input[name='att_comnum']").val(commattnum);
    commattnum++;
    $("#comatt").append(line);   
}

var commdefnum = 1;
function addComDef() {
    var line = "<tr id='df_com_"+commdefnum+"'>"
    line += "<td><input type='text' name='df_com_name_"+commdefnum+"' value='Commander "+commdefnum+"' maxlength='20' class='commandername' /></td>";
    line += "<td>"+select_country("df_com_country_"+commdefnum)+"</td>";
    line +="<td>"+select_attribute("df_com_s_"+commdefnum)+"</td>";
    line +="<td>"+select_attribute("df_com_t_"+commdefnum)+"</td>";
    line +="<td>"+select_attribute("df_com_mt_"+commdefnum)+"</td>";
    line += "<td>"+select_veteran("df_com_v_"+commdefnum)+"</td>";
    line +="<td><input type='radio' name='df_com_section_"+commdefnum+"' value='main' checked='checked' /></td>";    
    line +="<td><input type='radio' name='df_com_section_"+commdefnum+"' value='reinforce_1' /></td>";
    line +="<td><input type='radio' name='df_com_section_"+commdefnum+"' value='reinforce_2' /></td>";
    line +="<td><input type='radio' name='df_com_section_"+commdefnum+"' value='reinforce_3' /></td>";
    line +="<td><input type='radio' name='df_com_section_"+commdefnum+"' value='reinforce_4' /></td>";
    line +="<td><input type='radio' name='df_com_section_"+commdefnum+"' value='reinforce_5' /></td>";
    line +="<td><input type='button' onclick=\"removeEntry('df_com_"+commdefnum+"');\" value='-' /></td>";
    line += "</tr>";
    $("input[name='df_comnum']").val(commdefnum);
    commdefnum++;
    $("#comdef").append(line);
}

/* Commander attributes */
function select_attribute(name) {
    return "<select name='"+name+"' class='comunit' >"+
    "<option value='1'>1</option>"+
    "<option value='2'>2</option>"+
    "<option value='3'>3</option>"+
    "<option value='4'>4</option>"+
    "<option value='5'>5</option>"+
    "</select>";
}

/* Veteran level */
function select_veteran(name) {
    return "<select name='"+name+"' class='comunit' >"+
    "<option value='A'>A</option>"+
    "<option value='B'>B</option>"+
    "<option value='C'>C</option>"+
    "<option value='D'>D</option>"+
    "</select>";
}

/* Generic methods */
function removeEntry(id) {
    $("#"+id).remove();
}

/* Terrain dropbox generation*/
function main_terrain() {
    var line = "<tr>";
    line += "<td>"+select_terrain('main_terrain')+"</td>";
    line += "<td>"+select_terrain('ar1_terrain')+"</td>";
    line += "<td>"+select_terrain('ar2_terrain')+"</td>";
    line += "<td>"+select_terrain('ar3_terrain')+"</td>";
    line += "<td>"+select_terrain('ar4_terrain')+"</td>";
    line += "<td>"+select_terrain('ar5_terrain')+"</td>";
    line += "<td>"+select_terrain('dr1_terrain')+"</td>";
    line += "<td>"+select_terrain('dr2_terrain')+"</td>";
    line += "<td>"+select_terrain('dr3_terrain')+"</td>";
    line += "<td>"+select_terrain('dr4_terrain')+"</td>";
    line += "<td>"+select_terrain('dr5_terrain')+"</td>";
    line += "</tr>";
    $("#mainterrain").append(line);
}

function select_terrain(name) {
    return "<select name='"+name+"' class='terrain' >"+
    "<option value='Normal'>Normal</option>"+
    "<option value='Forest'>Forest</option>"+
    "<option value='Mountain'>Mountain</option>"+
    "<option value='Desert'>Desert</option>"+
    "<option value='Marsh'>Marsh</option>"+
    "</select>";
}

/* Logs management */
function filterLogs() {   
    var type = $('#logfilter').val();
    if(type == "all") {
        $('div[name="oo"]').show();
        $('div[name="oc"]').show();
        $('div[name="oe"]').show();
        $('div[name="on"]').show();
        $('div[name="or"]').show();
        $('div[name="ow"]').show();
        $('div[name="od"]').show();
        $('div[name="ao"]').show();
        $('div[name="ac"]').show();
        $('div[name="ae"]').show();
        $('div[name="an"]').show();
        $('div[name="ar"]').show();
        $('div[name="aw"]').show();
        $('div[name="ad"]').show();
        $('div[name="so"]').show();
        $('div[name="sc"]').show();
        $('div[name="se"]').show();
        $('div[name="sn"]').show();
        $('div[name="sr"]').show();
        $('div[name="sw"]').show();
        $('div[name="sd"]').show();
        $('div[name="lo"]').show();
        $('div[name="lc"]').show();
        $('div[name="le"]').show();
        $('div[name="ln"]').show();
        $('div[name="lr"]').show();
        $('div[name="lw"]').show();
        $('div[name="ld"]').show();
        $('div[name="po"]').show();
        $('div[name="pc"]').show();
        $('div[name="pe"]').show();
        $('div[name="pn"]').show();
        $('div[name="pr"]').show();
        $('div[name="pw"]').show();
        $('div[name="pd"]').show();
    } else {
        $('div[name="oo"]').css('display','none');
        $('div[name="oc"]').css('display','none');
        $('div[name="oe"]').css('display','none');
        $('div[name="on"]').css('display','none');
        $('div[name="or"]').css('display','none');
        $('div[name="ow"]').css('display','none');
        $('div[name="od"]').css('display','none');
        $('div[name="ao"]').css('display','none');
        $('div[name="ac"]').css('display','none');
        $('div[name="ae"]').css('display','none');
        $('div[name="an"]').css('display','none');
        $('div[name="ar"]').css('display','none');
        $('div[name="aw"]').css('display','none');
        $('div[name="ad"]').css('display','none');
        $('div[name="so"]').css('display','none');
        $('div[name="sc"]').css('display','none');
        $('div[name="se"]').css('display','none');
        $('div[name="sn"]').css('display','none');
        $('div[name="sr"]').css('display','none');
        $('div[name="sw"]').css('display','none');
        $('div[name="sd"]').css('display','none');
        $('div[name="lo"]').css('display','none');
        $('div[name="lc"]').css('display','none');
        $('div[name="le"]').css('display','none');
        $('div[name="ln"]').css('display','none');
        $('div[name="lr"]').css('display','none');
        $('div[name="lw"]').css('display','none');
        $('div[name="ld"]').css('display','none');
        $('div[name="po"]').css('display','none');
        $('div[name="pc"]').css('display','none');
        $('div[name="pe"]').css('display','none');
        $('div[name="pn"]').css('display','none');
        $('div[name="pr"]').css('display','none');
        $('div[name="pw"]').css('display','none');
        $('div[name="pd"]').css('display','none');

        $('div[name="'+type+'"]').show();
    }    
}

/* Country list */
function select_country(name) {
    
    return "<select name='"+name+"' class='country' >"+
    "<option value='England'>England</option>"+
    "<option value='France'>France</option>"+
    "<option value='Spain'>Spain</option>"+
    "<option value='Turkey'>Turkey</option>"+
    "<option value='Prussia'>Prussia</option>"+
    "<option value='Prussia1810'>Prussia(1810)</option>"+
    "<option value='Russia'>Russia</option>"+
    "<option value='Austria'>Austria</option>"+
    "<option value='EnglandND'>England No Dominant</option>"+
    "<option value='FranceND'>France No Dominant</option>"+
    "<option value='SpainD'>Spain Dominant</option>"+
    "<option value='TurkeyD'>Turkey Dominant</option>"+
    "<option value='PrussiaD'>Prussia Dominant</option>"+
    "<option value='Prussia1810D'>Prussia(1810) Dominant</option>"+
    "<option value='RussiaD'>Russia Dominant</option>"+
    "<option value='AustriaD'>Austria Dominant</option>"+
    "<option value='Algeria-England'>Algeria (England)</option>"+
    "<option value='Algeria-France'>Algeria (France)</option>"+
    "<option value='Algeria-Spain'>Algeria (Spain)</option>"+
    "<option value='Algeria-Turkey'>Algeria (Turkey)</option>"+
    "<option value='Algeria-Prussia'>Algeria (Prussia)</option>"+
    "<option value='Algeria-Prussia1810'>Algeria (Prussia 1810)</option>"+
    "<option value='Algeria-Russia'>Algeria (Russia)</option>"+
    "<option value='Algeria-Austria'>Algeria (Austria)</option>"+
    "<option value='Algeria-EnglandND'>Algeria (England ND)</option>"+
    "<option value='Algeria-FranceND'>Algeria (France ND)</option>"+
    "<option value='Algeria-SpainD'>Algeria (Spain D)</option>"+
    "<option value='Algeria-TurkeyD'>Algeria (Turkey D)</option>"+
    "<option value='Algeria-PrussiaD'>Algeria (Prussia D)</option>"+
    "<option value='Algeria-Prussia1810D'>Algeria (Prussia 1810 D)</option>"+
    "<option value='Algeria-RussiaD'>Algeria (Russia D)</option>"+
    "<option value='Algeria-AustriaD'>Algeria (Austria D)</option>"+
    "<option value='Algeria-TurkeyOttoman'>Algeria (Ottoman Empire)</option>"+
    "<option value='Baden-England'>Baden (England)</option>"+
    "<option value='Baden-France'>Baden (France)</option>"+
    "<option value='Baden-Spain'>Baden (Spain)</option>"+
    "<option value='Baden-Turkey'>Baden (Turkey)</option>"+
    "<option value='Baden-Prussia'>Baden (Prussia)</option>"+
    "<option value='Baden-Prussia1810'>Baden (Prussia 1810)</option>"+
    "<option value='Baden-Russia'>Baden (Russia)</option>"+
    "<option value='Baden-Austria'>Baden (Austria)</option>"+
    "<option value='Baden-EnglandND'>Baden (England ND)</option>"+
    "<option value='Baden-FranceND'>Baden (France ND)</option>"+
    "<option value='Baden-SpainD'>Baden (Spain D)</option>"+
    "<option value='Baden-TurkeyD'>Baden (Turkey D)</option>"+
    "<option value='Baden-PrussiaD'>Baden (Prussia D)</option>"+
    "<option value='Baden-Prussia1810D'>Baden (Prussia 1810 D)</option>"+
    "<option value='Baden-RussiaD'>Baden (Russia D)</option>"+
    "<option value='Baden-AustriaD'>Baden (Austria D)</option>"+
    "<option value='Bavaria-England'>Bavaria (England)</option>"+
    "<option value='Bavaria-France'>Bavaria (France)</option>"+
    "<option value='Bavaria-Spain'>Bavaria (Spain)</option>"+
    "<option value='Bavaria-Turkey'>Bavaria (Turkey)</option>"+
    "<option value='Bavaria-Prussia'>Bavaria (Prussia)</option>"+
    "<option value='Bavaria-Prussia1810'>Bavaria (Prussia 1810)</option>"+
    "<option value='Bavaria-Russia'>Bavaria (Russia)</option>"+
    "<option value='Bavaria-Austria'>Bavaria (Austria)</option>"+
    "<option value='Bavaria-EnglandND'>Bavaria (England ND)</option>"+
    "<option value='Bavaria-FranceND'>Bavaria (France ND)</option>"+
    "<option value='Bavaria-SpainD'>Bavaria (Spain D)</option>"+
    "<option value='Bavaria-TurkeyD'>Bavaria (Turkey D)</option>"+
    "<option value='Bavaria-PrussiaD'>Bavaria (Prussia D)</option>"+
    "<option value='Bavaria-Prussia1810D'>Bavaria (Prussia 1810 D)</option>"+
    "<option value='Bavaria-RussiaD'>Bavaria (Russia D)</option>"+
    "<option value='Bavaria-AustriaD'>Bavaria (Austria D)</option>"+
    "<option value='Cyrenaica-England'>Cyrenaica (England)</option>"+
    "<option value='Cyrenaica-France'>Cyrenaica (France)</option>"+
    "<option value='Cyrenaica-Spain'>Cyrenaica (Spain)</option>"+
    "<option value='Cyrenaica-Turkey'>Cyrenaica (Turkey)</option>"+
    "<option value='Cyrenaica-Prussia'>Cyrenaica (Prussia)</option>"+
    "<option value='Cyrenaica-Prussia1810'>Cyrenaica (Prussia 1810)</option>"+
    "<option value='Cyrenaica-Russia'>Cyrenaica (Russia)</option>"+
    "<option value='Cyrenaica-Austria'>Cyrenaica (Austria)</option>"+
    "<option value='Cyrenaica-EnglandND'>Cyrenaica (England ND)</option>"+
    "<option value='Cyrenaica-FranceND'>Cyrenaica (France ND)</option>"+
    "<option value='Cyrenaica-SpainD'>Cyrenaica (Spain D)</option>"+
    "<option value='Cyrenaica-TurkeyD'>Cyrenaica (Turkey D)</option>"+
    "<option value='Cyrenaica-PrussiaD'>Cyrenaica (Prussia D)</option>"+
    "<option value='Cyrenaica-Prussia1810D'>Cyrenaica (Prussia 1810 D)</option>"+
    "<option value='Cyrenaica-RussiaD'>Cyrenaica (Russia D)</option>"+
    "<option value='Cyrenaica-AustriaD'>Cyrenaica (Austria D)</option>"+
    "<option value='Cyrenaica-TurkeyOttoman'>Cyrenaica (Ottoman Empire)</option>"+
    "<option value='Denmark-England'>Denmark (England)</option>"+
    "<option value='Denmark-France'>Denmark (France)</option>"+
    "<option value='Denmark-Spain'>Denmark (Spain)</option>"+
    "<option value='Denmark-Turkey'>Denmark (Turkey)</option>"+
    "<option value='Denmark-Prussia'>Denmark (Prussia)</option>"+
    "<option value='Denmark-Prussia1810'>Denmark (Prussia 1810)</option>"+
    "<option value='Denmark-Russia'>Denmark (Russia)</option>"+
    "<option value='Denmark-Austria'>Denmark (Austria)</option>"+
    "<option value='Denmark-EnglandND'>Denmark (England ND)</option>"+
    "<option value='Denmark-FranceND'>Denmark (France ND)</option>"+
    "<option value='Denmark-SpainD'>Denmark (Spain D)</option>"+
    "<option value='Denmark-TurkeyD'>Denmark (Turkey D)</option>"+
    "<option value='Denmark-PrussiaD'>Denmark (Prussia D)</option>"+
    "<option value='Denmark-Prussia1810D'>Denmark (Prussia 1810 D)</option>"+
    "<option value='Denmark-RussiaD'>Denmark (Russia D)</option>"+
    "<option value='Denmark-AustriaD'>Denmark (Austria D)</option>"+
    "<option value='Egypt-England'>Egypt (England)</option>"+
    "<option value='Egypt-France'>Egypt (France)</option>"+
    "<option value='Egypt-Spain'>Egypt (Spain)</option>"+
    "<option value='Egypt-Turkey'>Egypt (Turkey)</option>"+
    "<option value='Egypt-Prussia'>Egypt (Prussia)</option>"+
    "<option value='Egypt-Prussia1810'>Egypt (Prussia 1810)</option>"+
    "<option value='Egypt-Russia'>Egypt (Russia)</option>"+
    "<option value='Egypt-Austria'>Egypt (Austria)</option>"+
    "<option value='Egypt-EnglandND'>Egypt (England ND)</option>"+
    "<option value='Egypt-FranceND'>Egypt (France ND)</option>"+
    "<option value='Egypt-SpainD'>Egypt (Spain D)</option>"+
    "<option value='Egypt-TurkeyD'>Egypt (Turkey D)</option>"+
    "<option value='Egypt-PrussiaD'>Egypt (Prussia D)</option>"+
    "<option value='Egypt-Prussia1810D'>Egypt (Prussia 1810 D)</option>"+
    "<option value='Egypt-RussiaD'>Egypt (Russia D)</option>"+
    "<option value='Egypt-AustriaD'>Egypt (Austria D)</option>"+
    "<option value='Egypt-TurkeyOttoman'>Egypt (Ottoman Empire)</option>"+
    "<option value='Hanover-England'>Hanover (England)</option>"+
    "<option value='HanoverGB-England'>Hanover (England training)</option>"+
    "<option value='Hanover-France'>Hanover (France)</option>"+
    "<option value='Hanover-Spain'>Hanover (Spain)</option>"+
    "<option value='Hanover-Turkey'>Hanover (Turkey)</option>"+
    "<option value='Hanover-Prussia'>Hanover (Prussia)</option>"+
    "<option value='Hanover-Prussia1810'>Hanover (Prussia 1810)</option>"+
    "<option value='Hanover-Russia'>Hanover (Russia)</option>"+
    "<option value='Hanover-Austria'>Hanover (Austria)</option>"+
    "<option value='Hanover-EnglandND'>Hanover (England ND)</option>"+
    "<option value='Hanover-FranceND'>Hanover (France ND)</option>"+
    "<option value='Hanover-SpainD'>Hanover (Spain D)</option>"+
    "<option value='Hanover-TurkeyD'>Hanover (Turkey D)</option>"+
    "<option value='Hanover-PrussiaD'>Hanover (Prussia D)</option>"+
    "<option value='Hanover-Prussia1810D'>Hanover (Prussia 1810 D)</option>"+
    "<option value='Hanover-RussiaD'>Hanover (Russia D)</option>"+
    "<option value='Hanover-AustriaD'>Hanover (Austria D)</option>"+
    "<option value='Hesse-England'>Hesse (England)</option>"+
    "<option value='Hesse-France'>Hesse (France)</option>"+
    "<option value='Hesse-Spain'>Hesse (Spain)</option>"+
    "<option value='Hesse-Turkey'>Hesse (Turkey)</option>"+
    "<option value='Hesse-Prussia'>Hesse (Prussia)</option>"+
    "<option value='Hesse-Prussia1810'>Hesse (Prussia 1810)</option>"+
    "<option value='Hesse-Russia'>Hesse (Russia)</option>"+
    "<option value='Hesse-Austria'>Hesse (Austria)</option>"+
    "<option value='Hesse-EnglandND'>Hesse (England ND)</option>"+
    "<option value='Hesse-FranceND'>Hesse (France ND)</option>"+
    "<option value='Hesse-SpainD'>Hesse (Spain D)</option>"+
    "<option value='Hesse-TurkeyD'>Hesse (Turkey D)</option>"+
    "<option value='Hesse-PrussiaD'>Hesse (Prussia D)</option>"+
    "<option value='Hesse-Prussia1810D'>Hesse (Prussia 1810 D)</option>"+
    "<option value='Hesse-RussiaD'>Hesse (Russia D)</option>"+
    "<option value='Hesse-AustriaD'>Hesse (Austria D)</option>"+
    "<option value='Holland-England'>Holland (England)</option>"+
    "<option value='Holland-France'>Holland (France)</option>"+
    "<option value='Holland-Spain'>Holland (Spain)</option>"+
    "<option value='Holland-Turkey'>Holland (Turkey)</option>"+
    "<option value='Holland-Prussia'>Holland (Prussia)</option>"+
    "<option value='Holland-Prussia1810'>Holland (Prussia 1810)</option>"+
    "<option value='Holland-Russia'>Holland (Russia)</option>"+
    "<option value='Holland-Austria'>Holland (Austria)</option>"+
    "<option value='Holland-EnglandND'>Holland (England ND)</option>"+
    "<option value='Holland-FranceND'>Holland (France ND)</option>"+
    "<option value='Holland-SpainD'>Holland (Spain D)</option>"+
    "<option value='Holland-TurkeyD'>Holland (Turkey D)</option>"+
    "<option value='Holland-PrussiaD'>Holland (Prussia D)</option>"+
    "<option value='Holland-Prussia1810D'>Holland (Prussia 1810 D)</option>"+
    "<option value='Holland-RussiaD'>Holland (Russia D)</option>"+
    "<option value='Holland-AustriaD'>Holland (Austria D)</option>"+
    "<option value='Lombardy-England'>Lombardy (England)</option>"+
    "<option value='Lombardy-France'>Lombardy (France)</option>"+
    "<option value='Lombardy-Spain'>Lombardy (Spain)</option>"+
    "<option value='Lombardy-Turkey'>Lombardy (Turkey)</option>"+
    "<option value='Lombardy-Prussia'>Lombardy (Prussia)</option>"+
    "<option value='Lombardy-Prussia1810'>Lombardy (Prussia 1810)</option>"+
    "<option value='Lombardy-Russia'>Lombardy (Russia)</option>"+
    "<option value='Lombardy-Austria'>Lombardy (Austria)</option>"+
    "<option value='Lombardy-EnglandND'>Lombardy (England ND)</option>"+
    "<option value='Lombardy-FranceND'>Lombardy (France ND)</option>"+
    "<option value='Lombardy-SpainD'>Lombardy (Spain D)</option>"+
    "<option value='Lombardy-TurkeyD'>Lombardy (Turkey D)</option>"+
    "<option value='Lombardy-PrussiaD'>Lombardy (Prussia D)</option>"+
    "<option value='Lombardy-Prussia1810D'>Lombardy (Prussia 1810 D)</option>"+
    "<option value='Lombardy-RussiaD'>Lombardy (Russia D)</option>"+
    "<option value='Lombardy-AustriaD'>Lombardy (Austria D)</option>"+
    "<option value='Morrocco-England'>Morrocco (England)</option>"+
    "<option value='Morrocco-France'>Morrocco (France)</option>"+
    "<option value='Morrocco-Spain'>Morrocco (Spain)</option>"+
    "<option value='Morrocco-Turkey'>Morrocco (Turkey)</option>"+
    "<option value='Morrocco-Prussia'>Morrocco (Prussia)</option>"+
    "<option value='Morrocco-Prussia1810'>Morrocco (Prussia 1810)</option>"+
    "<option value='Morrocco-Russia'>Morrocco (Russia)</option>"+
    "<option value='Morrocco-Austria'>Morrocco (Austria)</option>"+
    "<option value='Morrocco-EnglandND'>Morrocco (England ND)</option>"+
    "<option value='Morrocco-FranceND'>Morrocco (France ND)</option>"+
    "<option value='Morrocco-SpainD'>Morrocco (Spain D)</option>"+
    "<option value='Morrocco-TurkeyD'>Morrocco (Turkey D)</option>"+
    "<option value='Morrocco-PrussiaD'>Morrocco (Prussia D)</option>"+
    "<option value='Morrocco-Prussia1810D'>Morrocco (Prussia 1810 D)</option>"+
    "<option value='Morrocco-RussiaD'>Morrocco (Russia D)</option>"+
    "<option value='Morrocco-AustriaD'>Morrocco (Austria D)</option>"+
    "<option value='Morrocco-TurkeyOttoman'>Morrocco (Ottoman Empire)</option>"+
    "<option value='Naples-England'>Naples (England)</option>"+
    "<option value='Naples-France'>Naples (France)</option>"+
    "<option value='Naples-Spain'>Naples (Spain)</option>"+
    "<option value='Naples-Turkey'>Naples (Turkey)</option>"+
    "<option value='Naples-Prussia'>Naples (Prussia)</option>"+
    "<option value='Naples-Prussia1810'>Naples (Prussia 1810)</option>"+
    "<option value='Naples-Russia'>Naples (Russia)</option>"+
    "<option value='Naples-Austria'>Naples (Austria)</option>"+
    "<option value='Naples-EnglandND'>Naples (England ND)</option>"+
    "<option value='Naples-FranceND'>Naples (France ND)</option>"+
    "<option value='Naples-SpainD'>Naples (Spain D)</option>"+
    "<option value='Naples-TurkeyD'>Naples (Turkey D)</option>"+
    "<option value='Naples-PrussiaD'>Naples (Prussia D)</option>"+
    "<option value='Naples-Prussia1810D'>Naples (Prussia 1810 D)</option>"+
    "<option value='Naples-RussiaD'>Naples (Russia D)</option>"+
    "<option value='Naples-AustriaD'>Naples (Austria D)</option>"+
    "<option value='Piedmont-England'>Piedmont (England)</option>"+
    "<option value='Piedmont-France'>Piedmont (France)</option>"+
    "<option value='Piedmont-Spain'>Piedmont (Spain)</option>"+
    "<option value='Piedmont-Turkey'>Piedmont (Turkey)</option>"+
    "<option value='Piedmont-Prussia'>Piedmont (Prussia)</option>"+
    "<option value='Piedmont-Prussia1810'>Piedmont (Prussia 1810)</option>"+
    "<option value='Piedmont-Russia'>Piedmont (Russia)</option>"+
    "<option value='Piedmont-Austria'>Piedmont (Austria)</option>"+
    "<option value='Piedmont-EnglandND'>Piedmont (England ND)</option>"+
    "<option value='Piedmont-FranceND'>Piedmont (France ND)</option>"+
    "<option value='Piedmont-SpainD'>Piedmont (Spain D)</option>"+
    "<option value='Piedmont-TurkeyD'>Piedmont (Turkey D)</option>"+
    "<option value='Piedmont-PrussiaD'>Piedmont (Prussia D)</option>"+
    "<option value='Piedmont-Prussia1810D'>Piedmont (Prussia 1810 D)</option>"+
    "<option value='Piedmont-RussiaD'>Piedmont (Russia D)</option>"+
    "<option value='Piedmont-AustriaD'>Piedmont (Austria D)</option>"+
    "<option value='Poland-England'>Poland (England)</option>"+
    "<option value='Poland-France'>Poland (France)</option>"+
    "<option value='Poland-Spain'>Poland (Spain)</option>"+
    "<option value='Poland-Turkey'>Poland (Turkey)</option>"+
    "<option value='Poland-Prussia'>Poland (Prussia)</option>"+
    "<option value='Poland-Prussia1810'>Poland (Prussia 1810)</option>"+
    "<option value='Poland-Russia'>Poland (Russia)</option>"+
    "<option value='Poland-Austria'>Poland (Austria)</option>"+
    "<option value='Poland-EnglandND'>Poland (England ND)</option>"+
    "<option value='Poland-FranceND'>Poland (France ND)</option>"+
    "<option value='Poland-SpainD'>Poland (Spain D)</option>"+
    "<option value='Poland-TurkeyD'>Poland (Turkey D)</option>"+
    "<option value='Poland-PrussiaD'>Poland (Prussia D)</option>"+
    "<option value='Poland-Prussia1810D'>Poland (Prussia 1810 D)</option>"+
    "<option value='Poland-RussiaD'>Poland (Russia D)</option>"+
    "<option value='Poland-AustriaD'>Poland (Austria D)</option>"+
    "<option value='Portugal-England'>Portugal (England)</option>"+
    "<option value='PortugalGB-England'>Portugal (England training)</option>"+
    "<option value='Portugal-France'>Portugal (France)</option>"+
    "<option value='Portugal-Spain'>Portugal (Spain)</option>"+
    "<option value='Portugal-Turkey'>Portugal (Turkey)</option>"+
    "<option value='Portugal-Prussia'>Portugal (Prussia)</option>"+
    "<option value='Portugal-Prussia1810'>Portugal (Prussia 1810)</option>"+
    "<option value='Portugal-Russia'>Portugal (Russia)</option>"+
    "<option value='Portugal-Austria'>Portugal (Austria)</option>"+
    "<option value='Portugal-EnglandND'>Portugal (England ND)</option>"+
    "<option value='Portugal-FranceND'>Portugal (France ND)</option>"+
    "<option value='Portugal-SpainD'>Portugal (Spain D)</option>"+
    "<option value='Portugal-TurkeyD'>Portugal (Turkey D)</option>"+
    "<option value='Portugal-PrussiaD'>Portugal (Prussia D)</option>"+
    "<option value='Portugal-Prussia1810D'>Portugal (Prussia 1810 D)</option>"+
    "<option value='Portugal-RussiaD'>Portugal (Russia D)</option>"+
    "<option value='Portugal-AustriaD'>Portugal (Austria D)</option>"+
    "<option value='Saxony-England'>Saxony (England)</option>"+
    "<option value='Saxony-France'>Saxony (France)</option>"+
    "<option value='Saxony-Spain'>Saxony (Spain)</option>"+
    "<option value='Saxony-Turkey'>Saxony (Turkey)</option>"+
    "<option value='Saxony-Prussia'>Saxony (Prussia)</option>"+
    "<option value='Saxony-Prussia1810'>Saxony (Prussia 1810)</option>"+
    "<option value='Saxony-Russia'>Saxony (Russia)</option>"+
    "<option value='Saxony-Austria'>Saxony (Austria)</option>"+
    "<option value='Saxony-EnglandND'>Saxony (England ND)</option>"+
    "<option value='Saxony-FranceND'>Saxony (France ND)</option>"+
    "<option value='Saxony-SpainD'>Saxony (Spain D)</option>"+
    "<option value='Saxony-TurkeyD'>Saxony (Turkey D)</option>"+
    "<option value='Saxony-PrussiaD'>Saxony (Prussia D)</option>"+
    "<option value='Saxony-Prussia1810D'>Saxony (Prussia 1810 D)</option>"+
    "<option value='Saxony-RussiaD'>Saxony (Russia D)</option>"+
    "<option value='Saxony-AustriaD'>Saxony (Austria D)</option>"+
    "<option value='Sweden-England'>Sweden (England)</option>"+
    "<option value='Sweden-France'>Sweden (France)</option>"+
    "<option value='Sweden-Spain'>Sweden (Spain)</option>"+
    "<option value='Sweden-Turkey'>Sweden (Turkey)</option>"+
    "<option value='Sweden-Prussia'>Sweden (Prussia)</option>"+
    "<option value='Sweden-Prussia1810'>Sweden (Prussia 1810)</option>"+
    "<option value='Sweden-Russia'>Sweden (Russia)</option>"+
    "<option value='Sweden-Austria'>Sweden (Austria)</option>"+
    "<option value='Sweden-EnglandND'>Sweden (England ND)</option>"+
    "<option value='Sweden-FranceND'>Sweden (France ND)</option>"+
    "<option value='Sweden-SpainD'>Sweden (Spain D)</option>"+
    "<option value='Sweden-TurkeyD'>Sweden (Turkey D)</option>"+
    "<option value='Sweden-PrussiaD'>Sweden (Prussia D)</option>"+
    "<option value='Sweden-Prussia1810D'>Sweden (Prussia 1810 D)</option>"+
    "<option value='Sweden-RussiaD'>Sweden (Russia D)</option>"+
    "<option value='Sweden-AustriaD'>Sweden (Austria D)</option>"+
    "<option value='Syria-England'>Syria (England)</option>"+
    "<option value='Syria-France'>Syria (France)</option>"+
    "<option value='Syria-Spain'>Syria (Spain)</option>"+
    "<option value='Syria-Turkey'>Syria (Turkey)</option>"+
    "<option value='Syria-Prussia'>Syria (Prussia)</option>"+
    "<option value='Syria-Prussia1810'>Syria (Prussia 1810)</option>"+
    "<option value='Syria-Russia'>Syria (Russia)</option>"+
    "<option value='Syria-Austria'>Syria (Austria)</option>"+
    "<option value='Syria-EnglandND'>Syria (England ND)</option>"+
    "<option value='Syria-FranceND'>Syria (France ND)</option>"+
    "<option value='Syria-SpainD'>Syria (Spain D)</option>"+
    "<option value='Syria-TurkeyD'>Syria (Turkey D)</option>"+
    "<option value='Syria-PrussiaD'>Syria (Prussia D)</option>"+
    "<option value='Syria-Prussia1810D'>Syria (Prussia 1810 D)</option>"+
    "<option value='Syria-RussiaD'>Syria (Russia D)</option>"+
    "<option value='Syria-AustriaD'>Syria (Austria D)</option>"+
    "<option value='Syria-TurkeyOttoman'>Syria (Ottoman Empire)</option>"+
    "<option value='Tripolitania-England'>Tripolitania (England)</option>"+
    "<option value='Tripolitania-France'>Tripolitania (France)</option>"+
    "<option value='Tripolitania-Spain'>Tripolitania (Spain)</option>"+
    "<option value='Tripolitania-Turkey'>Tripolitania (Turkey)</option>"+
    "<option value='Tripolitania-Prussia'>Tripolitania (Prussia)</option>"+
    "<option value='Tripolitania-Prussia1810'>Tripolitania (Prussia 1810)</option>"+
    "<option value='Tripolitania-Russia'>Tripolitania (Russia)</option>"+
    "<option value='Tripolitania-Austria'>Tripolitania (Austria)</option>"+
    "<option value='Tripolitania-EnglandND'>Tripolitania (England ND)</option>"+
    "<option value='Tripolitania-FranceND'>Tripolitania (France ND)</option>"+
    "<option value='Tripolitania-SpainD'>Tripolitania (Spain D)</option>"+
    "<option value='Tripolitania-TurkeyD'>Tripolitania (Turkey D)</option>"+
    "<option value='Tripolitania-PrussiaD'>Tripolitania (Prussia D)</option>"+
    "<option value='Tripolitania-Prussia1810D'>Tripolitania (Prussia 1810 D)</option>"+
    "<option value='Tripolitania-RussiaD'>Tripolitania (Russia D)</option>"+
    "<option value='Tripolitania-AustriaD'>Tripolitania (Austria D)</option>"+
    "<option value='Tripolitania-TurkeyOttoman'>Tripolitania (Ottoman Empire)</option>"+
    "<option value='Tunisia-England'>Tunisia (England)</option>"+
    "<option value='Tunisia-France'>Tunisia (France)</option>"+
    "<option value='Tunisia-Spain'>Tunisia (Spain)</option>"+
    "<option value='Tunisia-Turkey'>Tunisia (Turkey)</option>"+
    "<option value='Tunisia-Prussia'>Tunisia (Prussia)</option>"+
    "<option value='Tunisia-Prussia1810'>Tunisia (Prussia 1810)</option>"+
    "<option value='Tunisia-Russia'>Tunisia (Russia)</option>"+
    "<option value='Tunisia-Austria'>Tunisia (Austria)</option>"+
    "<option value='Tunisia-EnglandND'>Tunisia (England ND)</option>"+
    "<option value='Tunisia-FranceND'>Tunisia (France ND)</option>"+
    "<option value='Tunisia-SpainD'>Tunisia (Spain D)</option>"+
    "<option value='Tunisia-TurkeyD'>Tunisia (Turkey D)</option>"+
    "<option value='Tunisia-PrussiaD'>Tunisia (Prussia D)</option>"+
    "<option value='Tunisia-Prussia1810D'>Tunisia (Prussia 1810 D)</option>"+
    "<option value='Tunisia-RussiaD'>Tunisia (Russia D)</option>"+
    "<option value='Tunisia-AustriaD'>Tunisia (Austria D)</option>"+
    "<option value='Tunisia-TurkeyOttoman'>Tunisia (Ottoman Empire)</option>"+
    "<option value='Venetia-England'>Venetia (England)</option>"+
    "<option value='Venetia-France'>Venetia (France)</option>"+
    "<option value='Venetia-Spain'>Venetia (Spain)</option>"+
    "<option value='Venetia-Turkey'>Venetia (Turkey)</option>"+
    "<option value='Venetia-Prussia'>Venetia (Prussia)</option>"+
    "<option value='Venetia-Prussia1810'>Venetia (Prussia 1810)</option>"+
    "<option value='Venetia-Russia'>Venetia (Russia)</option>"+
    "<option value='Venetia-Austria'>Venetia (Austria)</option>"+
    "<option value='Venetia-EnglandND'>Venetia (England ND)</option>"+
    "<option value='Venetia-FranceND'>Venetia (France ND)</option>"+
    "<option value='Venetia-SpainD'>Venetia (Spain D)</option>"+
    "<option value='Venetia-TurkeyD'>Venetia (Turkey D)</option>"+
    "<option value='Venetia-PrussiaD'>Venetia (Prussia D)</option>"+
    "<option value='Venetia-Prussia1810D'>Venetia (Prussia 1810 D)</option>"+
    "<option value='Venetia-RussiaD'>Venetia (Russia D)</option>"+
    "<option value='Venetia-AustriaD'>Venetia (Austria D)</option>"+
    "<option value='Wurttemburg-England'>Wurttemburg (England)</option>"+
    "<option value='Wurttemburg-France'>Wurttemburg (France)</option>"+
    "<option value='Wurttemburg-Spain'>Wurttemburg (Spain)</option>"+
    "<option value='Wurttemburg-Turkey'>Wurttemburg (Turkey)</option>"+
    "<option value='Wurttemburg-Prussia'>Wurttemburg (Prussia)</option>"+
    "<option value='Wurttemburg-Prussia1810'>Wurttemburg (Prussia 1810)</option>"+
    "<option value='Wurttemburg-Russia'>Wurttemburg (Russia)</option>"+
    "<option value='Wurttemburg-Austria'>Wurttemburg (Austria)</option>"+
    "<option value='Wurttemburg-EnglandND'>Wurttemburg (England ND)</option>"+
    "<option value='Wurttemburg-FranceND'>Wurttemburg (France ND)</option>"+
    "<option value='Wurttemburg-SpainD'>Wurttemburg (Spain D)</option>"+
    "<option value='Wurttemburg-TurkeyD'>Wurttemburg (Turkey D)</option>"+
    "<option value='Wurttemburg-PrussiaD'>Wurttemburg (Prussia D)</option>"+
    "<option value='Wurttemburg-Prussia1810D'>Wurttemburg (Prussia 1810 D)</option>"+
    "<option value='Wurttemburg-RussiaD'>Wurttemburg (Russia D)</option>"+
    "<option value='Wurttemburg-AustriaD'>Wurttemburg (Austria D)</option>"+
    "</select>";
}
