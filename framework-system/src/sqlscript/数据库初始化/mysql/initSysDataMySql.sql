-- mysql -u tfzc -D tfzcpt -p <D:\Projects\j2eews\tfzcpt\src\sqlScript\sysDataInit.sql

--�����û�
delete from f_userinfo;

insert into f_userinfo (USERCODE, USERPIN, ISVALID, LOGINNAME, USERNAME, USERDESC, LOGINTIMES, ACTIVETIME, LOGINIP, ADDRBOOKID, REGEMAIL, USERORDER, USERPWD, REGCELLPHONE, CREATEDATE,CREATOR,UPDATOR,UPDATEDATE)
values ('noname', '67b74fe1423796dfe8db34b959b81fbd', 'F', 'noname', '�����û�', '�����û�', null, null, '', null, 'noname@centit.com', 1, '', '', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000',now());
insert into f_userinfo (USERCODE, USERPIN, ISVALID, LOGINNAME, USERNAME, USERDESC, LOGINTIMES, ACTIVETIME, LOGINIP, ADDRBOOKID, REGEMAIL, USERORDER, USERPWD, REGCELLPHONE, CREATEDATE ,CREATOR,UPDATOR,UPDATEDATE)
values ('u0000000', 'd92ec26dec6234c5c79dfae76d375736', 'T', 'admin', '����Ա', '', null, null, '', null, 'codefan@centit.com', 1, '', '18017458877', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000',now());

--��ʼ�������ֵ�
--str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s')
insert into f_datacatalog (CATALOGCODE, CATALOGNAME, CATALOGSTYLE, CATALOGTYPE, CATALOGDESC, FIELDDESC, UPDATEDATE, CREATEDATE, OPTID, NEEDCACHE,CREATOR,UPDATOR)
values ('MsgType', '��Ϣ����', 'U', 'L', '���Բ���', null, str_to_date('25-02-2016 17:55:21', '%d-%m-%Y %H:%i:%s'), null, 'innermsg', '1','u0000000','u0000000');

insert into f_datacatalog (CATALOGCODE, CATALOGNAME, CATALOGSTYLE, CATALOGTYPE, CATALOGDESC, FIELDDESC, UPDATEDATE, CREATEDATE, OPTID, NEEDCACHE,CREATOR,UPDATOR)
values ('CatalogStyle', '�ֵ�����', 'S', 'L', 'F : ��ܹ��е� U:�û� S��ϵͳ', null, null, null, 'dictionary', '1','u0000000','u0000000');

insert into f_datacatalog (CATALOGCODE, CATALOGNAME, CATALOGSTYLE, CATALOGTYPE, CATALOGDESC, FIELDDESC, UPDATEDATE, CREATEDATE, OPTID, NEEDCACHE,CREATOR,UPDATOR)
values ('CatalogType', '�ֵ�ṹ', 'S', 'L', 'L:�б�T:���� �����޸�', null, str_to_date('01-12-2015 11:41:23', '%d-%m-%Y %H:%i:%s'), null, 'dictionary', '1','u0000000','u0000000');

insert into f_datacatalog (CATALOGCODE, CATALOGNAME, CATALOGSTYLE, CATALOGTYPE, CATALOGDESC, FIELDDESC, UPDATEDATE, CREATEDATE, OPTID, NEEDCACHE,CREATOR,UPDATOR)
values ('UnitType', '��λ����', 'U', 'L', '��λ����', null, null, null, null, '1','u0000000','u0000000');

insert into f_datacatalog (CATALOGCODE, CATALOGNAME, CATALOGSTYLE, CATALOGTYPE, CATALOGDESC, FIELDDESC, UPDATEDATE, CREATEDATE, OPTID, NEEDCACHE,CREATOR,UPDATOR)
values ('OptType', 'ҵ�����', 'S', 'L', 'ҵ�����', 'ҵ�����', null, null, '1', '1','u0000000','u0000000');

insert into f_datacatalog (CATALOGCODE, CATALOGNAME, CATALOGSTYLE, CATALOGTYPE, CATALOGDESC, FIELDDESC, UPDATEDATE, CREATEDATE, OPTID, NEEDCACHE,CREATOR,UPDATOR)
values ('StationType', '��λ��ɫ', 'U', 'L', 'ҵ���λ��������볤��Ϊ4', 'ҵ�����xx', null, null, '0', '0','u0000000','u0000000');

insert into f_datacatalog (CATALOGCODE, CATALOGNAME, CATALOGSTYLE, CATALOGTYPE, CATALOGDESC, FIELDDESC, UPDATEDATE, CREATEDATE, OPTID, NEEDCACHE,CREATOR,UPDATOR)
values ('RankType', '����ְ�����', 'U', 'L', 'ҵ��ְ����������볤��Ϊ2����ֵԽ�͵ȼ�Խ��', 'ְλ����;�ȼ�;δ��;ְλ����', null, null, null, '0','u0000000','u0000000');

insert into f_datacatalog (CATALOGCODE, CATALOGNAME, CATALOGSTYLE, CATALOGTYPE, CATALOGDESC, FIELDDESC, UPDATEDATE, CREATEDATE, OPTID, NEEDCACHE,CREATOR,UPDATOR)
values ('SUPPORT_LANG', 'ϵͳ֧�ֵ�����', 'U', 'L', 'ϵͳ֧�ֵ�����,��Ҫ��system.properties�аѲ���sys.multi_lang����Ϊtrue�Ż���Ч', null, str_to_date('28-01-2016 20:33:23', '%d-%m-%Y %H:%i:%s'), null, 'DICTSET', '1','u0000000','u0000000');

insert into f_datacatalog (CATALOGCODE, CATALOGNAME, CATALOGSTYLE, CATALOGTYPE, CATALOGDESC, FIELDDESC, UPDATEDATE, CREATEDATE, OPTID, NEEDCACHE,CREATOR,UPDATOR)
values ('LogLevel', '��־����', 'F', 'L', '��־����', '��־����', str_to_date('07-04-2016', '%d-%m-%Y'), str_to_date('07-04-2016', '%d-%m-%Y'), 'OptLog', '1','u0000000','u0000000');


insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('MsgType', 'P', null, null, 'T', '������Ϣ', 'U', null, str_to_date('25-02-2016 17:55:21', '%d-%m-%Y %H:%i:%s'), null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('MsgType', 'A', null, null, 'T', '����', 'U', '������Ⱥ������Ϣ', str_to_date('25-02-2016 17:55:21', '%d-%m-%Y %H:%i:%s'), null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('CatalogStyle', 'F', null, null, 'T', '��ܹ���', 'S', '�κεط���������༭��ֻ���п�����Ա�������½ű���ӡ����ĺ�ɾ��', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('CatalogStyle', 'G', null, null, 'T', '����', 'S', '�����ʱ�����ǿ������ֵ�����н�������', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('CatalogStyle', 'S', null, null, 'T', 'ϵͳ����', 'S', 'ʵʩ��Ա������ʵʩ��ڶ������ֵ�������ֵ���Ŀ����CRUD����', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('CatalogStyle', 'U', null, null, 'T', '�û�����', 'S', '����Ա��� �� ʵʩ��Ա��� �� ��������ֵ�������Ŀ����CRUD', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('CatalogType', 'L', null, null, 'T', '�б�', 'S', '�б�', str_to_date('01-12-2015 11:41:23', '%d-%m-%Y %H:%i:%s'), null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('CatalogType', 'T', null, null, 'T', '����', 'S', '����', str_to_date('01-12-2015 11:41:23', '%d-%m-%Y %H:%i:%s'), null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('UnitType', 'A', 'CCCC', null, 'T', '����', 'S', 'administrator', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('UnitType', 'L', 'BBB', null, 'T', '����', 'S', 'logistics', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('UnitType', 'O', 'DDD', null, 'T', 'ҵ��', 'S', 'operator', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('UnitType', 'R', 'A', null, 'T', '�з�', 'S', 'Rearch', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'IM', '3', null, 'T', '��Ŀ����', 'U', '��Ŀ����', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'GM', '1', null, 'T', '�ܾ���', null, null, null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'DM', '2', null, 'T', '���ž���', null, null, null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'EM', '9', null, 'T', 'Ա��', null, null, null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'KZ', '10', null, 'T', '�Ƴ�', null, '�Ƴ�', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'ZR', '10', null, 'T', '����', null, '����', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'KY', '10', null, 'T', '�칫�ҿ�Ա', null, '�칫�ҿ�Ա', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'CZ', '10', null, 'T', '����', null, '����', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'WDZ', '4', null, 'T', 'ί����', null, null, null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'WLD', '5', null, 'T', 'ί�쵼', null, 'ί�쵼', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'FC', '6', null, 'T', '������', null, '������', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'JJZ', '7', null, 'T', '�ͼ���', null, '�ͼ���', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('RankType', 'JGDW', '8', null, 'T', '���ص�ί', null, '���ص�ί', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'slbmfzr', null, null, 'T', '�����Ÿ�����', 'S', null, null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'fgjz', null, null, 'T', '�ֳֹܾ�', null, null, null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'sfy', null, null, 'T', '�շ�Ա', null, null, null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'xbr', null, null, 'T', 'Э�촦�Ҹ�����', null, 'Э�촦�Ҹ�����', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'bgszr', null, null, 'T', '�칫������', null, '�칫������', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'xkdj', null, null, 'T', '��ɵǼ�', 'S', '��ɵǼ�', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'xkbl', null, null, 'T', '��ɰ���', 'S', '��ɰ���', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'dcdb_jcry', null, null, 'T', '���鶽��_�����Ա', 'S', '���𶽰�ļ����Ա', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'dcdb_jbrld', null, null, 'T', '���鶽��_�������쵼', 'S', '�������˵ķֹ��쵼', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'dcdb_jcld', null, null, 'T', '���鶽��_����쵼', 'S', '���𶽰�ļ����Ա�ֹ��쵼', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'dcdb_jbr', null, null, 'T', '���鶽��_������', 'S', '��������', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'fzr', null, null, 'T', '���촦�Ҹ�����', null, '���촦�Ҹ�����', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'sjr', null, null, 'T', '�칫���ռ���', null, '�칫���ռ���', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'pf', null, null, 'T', '�칫��������', null, '�칫��������', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'cbr', null, null, 'T', '����а���', null, '����а���', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'syr', null, null, 'T', '�칫��������', null, '�칫��������', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'sr', null, null, 'T', '�칫����Ա', null, '�칫����Ա', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'fgzr', null, null, 'T', '�ֹ�����', null, '�ֹ�����', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'fw_nw', null, null, 'T', '��������', 'S', null, null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'wysry', null, null, 'T', '��ӡ����Ա', null, '��ӡ����Ա', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'wyszr', null, null, 'T', '��ӡ������', null, '��ӡ������', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'spcjbr', null, null, 'T', '������������', 'S', '������������', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'spccz', null, null, 'T', '����������', 'S', '����������', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'bgsms', null, null, 'T', '�칫������', 'S', '�칫������', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'bgsfgzr', null, null, 'T', '�칫�ҷֹ�����', 'S', '�칫�ҷֹ�����', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'wld', null, null, 'T', 'ί�쵼ǩ��', 'S', 'ί�쵼ǩ��', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('StationType', 'bgsfwh', null, null, 'T', '�칫���������ĺ�', 'S', '�칫���������ĺ�', null, null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('SUPPORT_LANG', 'zh_CN', null, null, 'T', '����', 'U', null, str_to_date('28-01-2016 20:33:23', '%d-%m-%Y %H:%i:%s'), null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('SUPPORT_LANG', 'en_US', null, null, 'T', 'English', 'U', null, str_to_date('28-01-2016 20:33:23', '%d-%m-%Y %H:%i:%s'), null, null);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('OptType', 'S', null, null, 'T', 'ʵʩҵ��', 'F', 'ʵʩҵ��', str_to_date('01-04-2015 01:00:00', '%d-%m-%Y %H:%i:%s'), str_to_date('01-04-2015 01:00:00', '%d-%m-%Y %H:%i:%s'), 2);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('OptType', 'O', null, null, 'T', '��ͨҵ��', 'F', '��ͨҵ��', str_to_date('01-04-2015 01:00:00', '%d-%m-%Y %H:%i:%s'), str_to_date('01-04-2015 01:00:00', '%d-%m-%Y %H:%i:%s'), 1);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('OptType', 'I', null, null, 'T', '��Ŀҵ��', 'F', '��Ŀҵ��', str_to_date('01-04-2015 01:00:00', '%d-%m-%Y %H:%i:%s'), str_to_date('01-04-2015 01:00:00', '%d-%m-%Y %H:%i:%s'), 3);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('OptType', 'W', null, null, 'T', '������ҵ��', 'F', '������ҵ��', str_to_date('01-04-2015 01:00:00', '%d-%m-%Y %H:%i:%s'), str_to_date('01-04-2015 01:00:00', '%d-%m-%Y %H:%i:%s'), 4);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('LogLevel', '1', null, null, 'T', '������ʾ', 'F', null, str_to_date('07-04-2016', '%d-%m-%Y'), str_to_date('07-04-2016', '%d-%m-%Y'), 2);

insert into f_datadictionary (CATALOGCODE, DATACODE, EXTRACODE, EXTRACODE2, DATATAG, DATAVALUE, DATASTYLE, DATADESC, LASTMODIFYDATE, CREATEDATE, DATAORDER)
values ('LogLevel', '0', null, null, 'T', '������־', 'F', null, str_to_date('07-04-2016', '%d-%m-%Y'), str_to_date('07-04-2016', '%d-%m-%Y'), 1);


--��ʼ��ҵ��˵�
insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('DEPTMAG', '���Ź���', '0', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'I', 'icon-base icon-base-computer', null, null, str_to_date('12-01-2016 17:04:01', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('DEPTPOW', '�������Ź���', 'DEPTMAG', 'modules/sys/deptpow/deptpow.html', '/system/deptManager', null, 'O', null, null, 'Y', null, null, 0, null, 'D', 'icon-base icon-base-user', null, null, str_to_date('12-01-2016 17:10:45', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('DEPTROLE', '���Ž�ɫ����', 'DEPTMAG', 'modules/sys/deptrole/deptrole.html', '/system/deptManager!', null, 'O', null, null, 'Y', null, null, 0, null, 'D', 'icon-base icon-base-gear', null, null, str_to_date('12-01-2016 17:10:41', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('DEPTUSERINFO', '�����û�����', 'DEPTMAG', 'modules/sys/deptuserinfo/deptuserinfo.html', '/system/userDef', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, str_to_date('12-01-2016 17:11:02', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('UNITMAG', '��������', 'ORGMAG', 'modules/sys/unitinfo/unitinfo.html', '/system/unitinfo', null, 'O', null, null, 'Y', null, null, 2, null, 'D', null, null, null, str_to_date('14-03-2016 14:41:07', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('USERMAG', '�û�����', 'ORGMAG', 'modules/sys/userinfo/userinfo.html', '/system/userinfo', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, str_to_date('18-02-2016 17:46:43', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('USERROLE', '�û���ɫ', 'ORGMAG', '/modules/sys/userrole.html', '/system/userrole', null, 'O', null, null, 'N', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('USERUNIT', '�û�����', 'ORGMAG', '/modules/sys/userunit.html', '/system/userunit', null, 'O', null, null, 'N', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('DICTSET_M', '�����ֵ�', 'SYS_CONFIG', 'modules/sys/dictionary/dictionary.html', '/system/dictionary', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), str_to_date('23-12-2014 14:04:59', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('OPT_LOG_QUERY', 'ϵͳ��־', 'SYS_CONFIG', 'modules/sys/loginfo/loginfo.html', '/system/optlog', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, str_to_date('27-11-2015 11:19:09', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('USER_SETTING', '��������', 'SYS_CONFIG', '/modules/sys/usersetting.html', '/system/usersetting', null, 'N', null, null, 'N', null, null, null, null, 'D', null, null, null, str_to_date('23-12-2014 16:52:40', '%d-%m-%Y %H:%i:%s'), str_to_date('23-12-2014 16:52:40', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('CALENDAR', '����', 'SYS_CONFIG', '/modules/sys/schedule/schedule.html', '/system/calendar', null, 'O', null, null, 'Y', null, null, null, null, 'D', 'icon-base icon-base-calendar', null, null, str_to_date('04-03-2015 09:55:31', '%d-%m-%Y %H:%i:%s'), str_to_date('04-03-2015 09:55:31', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('DEPLOY', 'ʵʩ�˵�', '0', '...', '...', null, 'S', null, null, 'Y', null, null, null, null, 'D', '444', null, null, str_to_date('15-12-2014 14:10:08', '%d-%m-%Y %H:%i:%s'), str_to_date('15-12-2014 14:10:08', '%d-%m-%Y %H:%i:%s'),'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('SYSCONF', 'ϵͳ����', 'DEPLOY', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'I', 'icon-base icon-base-gear', null, null, null, null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('DICTSET', '�����ֵ����', 'SYSCONF', 'modules/sys/dictionary/dictionary.admin.html', '/system/dictionary', null, 'S', null, null, 'Y', null, null, 0, null, 'D', 'icon-base icon-base-gear', null, null, str_to_date('18-02-2016 17:48:18', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('OPTINFO', 'ϵͳҵ��', 'SYSCONF', 'modules/sys/optinfo/optinfo.html', '/system/optinfo', null, 'S', null, null, 'Y', null, null, 4, null, 'D', null, null, null, str_to_date('30-01-2016 19:50:37', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('OPTLOG', 'ϵͳ��־', 'SYSCONF', 'modules/sys/loginfo/loginfo.admin.html', '/system/optlog', null, 'S', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('ROLEMAG', '��ɫ����', 'ORGMAG', 'modules/sys/roleinfo/roleinfo.html', '/system/roleinfo', null, 'O', null, null, 'Y', null, null, null, null, 'D', null, null, null, null, null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('ORGMAG', '��֯����', '0', '...', '...', null, 'O', null, null, 'Y', null, null, 3, null, 'I', 'icon-base icon-base-user', null, null, str_to_date('31-01-2016 15:55:53', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('SYS_CONFIG', 'ϵͳ����', '0', '...', '...', null, 'O', null, null, 'Y', null, null, null, null, 'D', 'icon-base icon-base-gear', null, null, null, null,'u0000000','u0000000');

insert into f_optinfo (OPTID, OPTNAME, PREOPTID, OPTROUTE, OPTURL, FORMCODE, OPTTYPE, MSGNO, MSGPRM, ISINTOOLBAR, IMGINDEX, TOPOPTID, ORDERIND, FLOWCODE, PAGETYPE, ICON, HEIGHT, WIDTH, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('LOGINCAS', 'CAS��¼���', '0', '/system/mainframe/logincas', '/system/mainframe', null, 'O', null, null, 'N', null, null, null, null, 'D', null, null, null, str_to_date('07-04-2016 15:06:08', '%d-%m-%Y %H:%i:%s'), null,'u0000000','u0000000');


insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000038', 'USER_SETTING', '����������û�����', null, null, null, null, null, '/*', 'CU','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000037', 'USER_SETTING', '��ȡ�û����ò���', null, '��ȡ��ǰ�û����õĲ���', null, null, null, '/*', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000039', 'USER_SETTING', 'ɾ���û����ò���', null, 'ɾ���û����ò���', null, null, null, '/*', 'D','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000040', 'USER_SETTING', '�����û����ò���', null, '�����û����ò���', null, null, null, '/export', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000051', 'USERUNIT', '��ȡ�����û�����������Ϣ', null, '��ȡ�����û�����������Ϣ', null, null, null, '/*/*', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000020', 'OPTLOG', '��ѯ', 'list', '��ѯϵͳ��־', null, null, null, '/*', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000041', 'OPT_LOG_QUERY', '�鿴��־����', null, '�鿴������־', null, str_to_date('27-11-2015 11:19:09', '%d-%m-%Y %H:%i:%s'), null, '/*', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000070', 'USERMAG', '�û��б�', null, '�û��б�', null, str_to_date('18-02-2016 17:46:43', '%d-%m-%Y %H:%i:%s'), null, '/', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000029', 'USERUNIT', '�����û���������', null, '����û���������', null, null, null, '/', 'C','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000032', 'USERROLE', '�����û���ɫ����', null, '����û�������ɫ', null, null, null, '/', 'C','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000030', 'USERUNIT', '�༭�û���������', null, '�����û�����������Ϣ', null, null, null, '/*/*', 'U','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000031', 'USERUNIT', 'ɾ���û���������', null, 'ɾ���û�������������', null, null, null, '/*/*', 'D','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000033', 'USERROLE', '�༭�û���ɫ����', null, '�����û�������ɫ��Ϣ', null, null, null, '/*/*', 'U','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000034', 'USERROLE', 'ɾ���û���ɫ����', null, 'ɾ���û�������ɫ', null, null, null, '/*/*', 'D','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000071', 'USERMAG', '�����û�', null, '�����û�', null, str_to_date('18-02-2016 17:46:44', '%d-%m-%Y %H:%i:%s'), null, '/', 'C','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000072', 'USERMAG', '�����û�', null, '�����û�', null, str_to_date('18-02-2016 17:46:44', '%d-%m-%Y %H:%i:%s'), null, '/*', 'U','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000073', 'USERMAG', 'ɾ���û�', null, 'ɾ���û�', null, str_to_date('18-02-2016 17:46:44', '%d-%m-%Y %H:%i:%s'), null, '/*', 'D','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000052', 'DICTSET', '��ѯ��������Ŀ¼', null, '��ѯ��������Ŀ¼', null, str_to_date('18-02-2016 17:48:18', '%d-%m-%Y %H:%i:%s'), null, '/*', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000053', 'DICTSET', '��ѯ���������ֵ�', null, '��ѯ���������ֵ�', null, str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/dictionary/*/*', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000054', 'DICTSET', '��ȡ���������������ֵ�', null, '��ȡ���������������ֵ�', null, str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/cache/*', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000009', 'DICTSET', 'ɾ�������ֵ�', 'DELTE', 'ɾ�������ֵ�', 'F', str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/dictionary/*/*', 'D','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000007', 'DICTSET', '�о��ֵ�', 'LIST', '��ʼҳ��', 'F', str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/data', 'U','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000008', 'DICTSET', '����/�༭�����ֵ�', 'EDIT', '����/�༭�����ֵ�', 'F', str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/dictionary/*/*', 'CU','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000010', 'DICTSET', '����/�༭����Ŀ¼', 'editDetail', '�༭/�½�����Ŀ¼', 'F', str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/*', 'CU','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000011', 'DICTSET', 'ɾ������Ŀ¼', 'deleteDetail', 'ɾ���ֵ�Ŀ¼', 'F', str_to_date('18-02-2016 17:48:19', '%d-%m-%Y %H:%i:%s'), null, '/*', 'D','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000042', 'DICTSET_M', '��ѯ��������Ŀ¼', null, '��ѯ��������Ŀ¼', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000043', 'DICTSET_M', '��ѯ���������ֵ�', null, '��ѯ���������ֵ�', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*/dictionary/*', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000044', 'DICTSET_M', '����/�༭����Ŀ¼', null, '����/�༭����Ŀ¼', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*', 'CU','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000045', 'DICTSET_M', '����/�༭�����ֵ�', null, '����/�༭�����ֵ�', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*/dictionary/*', 'CU','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000046', 'DICTSET_M', 'ɾ������Ŀ¼', null, 'ɾ������Ŀ¼', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*', 'D','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000047', 'DICTSET_M', 'ɾ�������ֵ�', null, 'ɾ�������ֵ�', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/*/dictionary/*', 'D','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000048', 'DICTSET_M', '��ȡ���������������ֵ�', null, '��ȡ���������������ֵ�', null, str_to_date('30-01-2016 19:53:38', '%d-%m-%Y %H:%i:%s'), null, '/cache/*', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000059', 'UNITMAG', '�鿴���л���', null, '�鿴���л���', null, str_to_date('14-03-2016 14:41:07', '%d-%m-%Y %H:%i:%s'), null, '/', 'R','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000001', 'UNITMAG', '�½�/�༭����', 'EDIT', '�½��͸��»���', 'F', str_to_date('14-03-2016 14:41:07', '%d-%m-%Y %H:%i:%s'), null, '/*', 'CU','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000002', 'UNITMAG', '����/�ָ�����', 'DELETE', '���»���״̬', 'F', str_to_date('14-03-2016 14:41:07', '%d-%m-%Y %H:%i:%s'), null, '/*/status/*', 'U','u0000000','u0000000');

insert into f_optdef (OPTCODE, OPTID, OPTNAME, OPTMETHOD, OPTDESC, ISINWORKFLOW, UPDATEDATE, CREATEDATE, OPTURL, OPTREQ,CREATOR,UPDATOR)
values ('1000080', 'LOGINCAS', 'CAS��¼���', null, null, null, null, null, '/logincas', 'RCU','u0000000','u0000000');

-- �û�����ɫ��Ȩ�޳�ʼ��


--��ʼ����ɫ��Ϣ
--��ʼ����ɫ��Ϣ
insert into f_roleinfo (ROLECODE, ROLENAME,ROLETYPE, ISVALID, ROLEDESC, CREATEDATE, UPDATEDATE,CREATOR,UPDATOR)
values ('G-DEPLOY', 'ʵʩ��Ա','S','T', 'ʵʩ��Ա��ɫ', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'), now(),'u0000000','u0000000');

insert into f_roleinfo (ROLECODE, ROLENAME,ROLETYPE,  ISVALID, ROLEDESC, CREATEDATE, UPDATEDATE,CREATOR,UPDATOR)
values ('G-SYSADMIN', 'ϵͳ����Ա','S', 'T', '����ϵͳ���ù���', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'), now(),'u0000000','u0000000');

insert into f_roleinfo (ROLECODE, ROLENAME,ROLETYPE,  ISVALID, ROLEDESC, CREATEDATE, UPDATEDATE,CREATOR,UPDATOR)
values ('G-anonymous', '������ɫ','S', 'T', '�����û���ɫ', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'), now(),'u0000000','u0000000');

insert into f_roleinfo (ROLECODE, ROLENAME,ROLETYPE,  ISVALID, ROLEDESC, CREATEDATE, UPDATEDATE,CREATOR,UPDATOR)
values ('G-public', '������ɫ','S', 'F', '������ɫȨ�޻�Ĭ�ϸ������������û���������', str_to_date('12-12-2014 16:05:46', '%d-%m-%Y %H:%i:%s'), now(),'u0000000','u0000000');


------
insert into f_rolepower (ROLECODE, OPTCODE, UPDATEDATE, CREATEDATE, OPTSCOPECODES,CREATOR,UPDATOR)
values ('G-public', '1000080', str_to_date('11-04-2016 10:21:17', '%d-%m-%Y %H:%i:%s'), str_to_date('11-04-2016 10:21:17', '%d-%m-%Y %H:%i:%s'), '','u0000000','u0000000');

SET SQL_SAFE_UPDATES = 0;

insert into f_optdef(optcode,optid,optname,optmethod,optdesc,
			isinworkflow,UPDATEDATE,createdate,opturl,optreq,CREATOR,UPDATOR)
select  sequence_nextval('S_OPTDEFCODE'),optid , '�鿴', 'list',  '�鿴',
		'F',now(),now(),'/*','R' ,CREATOR,UPDATOR
		from f_optinfo where optid not in (select optid from f_optdef);

insert into f_rolepower(rolecode,optcode,updateDate,createdate,optscopecodes,CREATOR,UPDATOR)
	select 'G-SYSADMIN',optcode,now(),now(),'',CREATOR,UPDATOR from f_optdef;

insert into f_userrole (USERCODE, ROLECODE, OBTAINDATE, 
			SECEDEDATE, CHANGEDESC, UPDATEDATE, CREATEDATE,CREATOR,UPDATOR)
values ('u0000000', 'G-SYSADMIN', STR_TO_DATE('23-05-2012','%d-%m-%Y'), 
	STR_TO_DATE('01-10-2020', '%d-%m-%Y'),'' ,now(), now(),'u0000000','u0000000');

