--
-- JBoss, Home of Professional Open Source
-- Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
-- contributors by the @authors tag. See the copyright.txt in the
-- distribution for a full listing of individual contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- http://www.apache.org/licenses/LICENSE-2.0
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- You can use this file to load seed data into the database using SQL statements

-- USUARIOS: clave decode : nueva
INSERT INTO public.users(user_type, id, firstname, lastname, picture, username, email, creation, password) VALUES ('CLIENT', 1, 'First', 'Last', './assets/avatars/avatar.png', 'usera', 'user@gmail.com','2018-06-22 05:39:49.916-03' ,'17950858528ec9887ed7b7ea91a703ad2b7b23acc6d65c1b54ffc7b77e02a2b718f9da6f86b1afdbd1de05758cd64f12e88182c1adf63de5c47095b55c2b3e8e');
INSERT INTO public.users(user_type, id, firstname, lastname, picture, username, email, creation, password) VALUES ('CLIENT', 2, 'First', 'Last', './assets/avatars/avatar.png', 'userb', 'user@gmail.com', '2018-06-22 05:39:49.916-03', '17950858528ec9f1fed7b7ea11a703ad2b7b23ac46d65c1b44ffc7b77e02a2b718f9da6f86b1afdbd1de05758cd64f12e88182c1adf63de5c47095b55c2b3e8e');

-- ACTUALIZO LAS SEQUENCIAS, AL ULTIMO ID INSERTADO
SELECT setval('users_id_seq', (SELECT max(id) FROM users), true);

SELECT setval('setting_id_seq', (SELECT max(id) FROM settings), true);