--mysql
alter table F_DATACATALOG modify FieldDesc varchar(1024);
alter table F_UNITINFO add UNITPATH VARCHAR(1000);

DELIMITER $$

CREATE FUNCTION calcUnitPath (chrId varchar(32)) 
	RETURNS varchar(1000) 
BEGIN
   DECLARE sTemp VARCHAR(32);
   DECLARE sPreTemp VARCHAR(32);
   DECLARE path VARCHAR(1000);
   DECLARE rs VARCHAR(1000);   
   SET  sTemp = trim(chrId);
   SET  path = '';
   REPEAT
   	  SET  path = concat('/',sTemp, path);
   	  set sPreTemp = sTemp;
      SELECT unitcode INTO sTemp 
         FROM f_unitinfo  
         where unitcode = 
         		(select parentunit FROM f_unitinfo where unitcode = sTemp);
      until sTemp is null or sTemp='' or sPreTemp = sTemp
   END REPEAT;
  
   RETURN path;
END$$

DELIMITER ;

SET SQL_SAFE_UPDATES = 0;
update f_unitinfo t set t.UnitPath = calcUnitPath(t.unitcode);

-- 添加触发器来维持这个 unitpath 新的版本程序中已经实现所以不需要这个触发器
insert 触发器
根据 parentUnit 获取 上级unit pu
如果存在 set unitPath = pu.unitPath + '/' +unitCode
否则 set unitPath = '/' +unitCode
------------------------------------
update 触发器
判断有没有更改 parentUnit 如果没变化 不做任何事，如果有变化
set oldUnitPath
根据 parentUnit 获取 上级unit pu
set newUnitPath = pu.unitPath + '/' +unitCode

update f_unitinfo b set b.UnitPath = newUnitPath + substrb(b.UnitPath ,LENGTHB(oldUnitPath)+1)
 where b.UnitPath like oldUnitPath||'/%'; 
------------
delete 触发器
 update f_unitinfo b set b.UnitPath =  substrb(b.UnitPath ,LENGTHB(UnitPath)+1)
 where b.UnitPath like UnitPath||'/%'; 

-- 请翻译一下
create or replace view v_hi_unitinfo as
select b.unitcode as topunitcode,  a.unitcode,a.unittype, a.parentunit, a.isvalid,
       a.unitname,a.unitdesc,a.unitshortname,a.addrbookid,a.unitorder,a.depno,
       a.unitword,a.unitgrade,
       LENGTHB(b.UnitPath)- LENGTHB(REPLACE(b.UnitPath,'/','')) - LENGTHB(a.UnitPath) + LENGTHB(REPLACE(a.UnitPath,'/',''))  as hi_level,
       substrb(b.UnitPath ,  LENGTHB(a.UnitPath)+1) as UnitPath
  from f_unitinfo a , f_unitinfo b
 where b.UnitPath like a.UnitPath||'/%'; 
 