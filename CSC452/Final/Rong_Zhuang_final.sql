2)
create or replace PROCEDURE update_expiration_date
(
  p_member_id           NUMBER,
  p_expiration_date DATE
)
IS
  v_count NUMBER;
BEGIN
  IF p_member_id <= 0 THEN
    DBMS_OUTPUT.PUT_LINE('Invalid member ID!');
    RETURN;
  END IF;

  SELECT COUNT(*)
    INTO v_count
    FROM member
   WHERE member_id = p_member_id;

  IF v_count = 0 THEN
    DBMS_OUTPUT.PUT_LINE('Invalid member ID!');
    RETURN;
  END IF;

  UPDATE member
     SET expiration_date = p_expiration_date,
         last_update_date = sysdate
   WHERE member_id = p_member_id;

  COMMIT;

  DBMS_OUTPUT.PUT_LINE('The expiration date has been updated.');

  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('My exception: ' ||
        TO_CHAR(SQLCODE) || '   ' || SQLERRM);
END;

3) create or replace PROCEDURE video_search
(
  p_video_name 	VARCHAR2,
  p_format 		VARCHAR2 DEFAULT NULL
)
IS
  CURSOR c_video IS
    SELECT v.video_name, c.video_copy_id, v.format, c.status, r.checkout_date, r.due_date
    FROM video v
    LEFT OUTER JOIN video_copy c
    ON v.video_id = c.video_id
    LEFT OUTER JOIN video_rental_record r
    ON c.video_copy_id = r.video_copy_id
    WHERE c.status <> 'D' AND UPPER(video_name) like '%' || UPPER(p_video_name) ||  '%'
    ORDER BY video_name, video_copy_id;
BEGIN
  DBMS_OUTPUT.PUT_LINE(RPAD('VIDEO NAME', 20) || RPAD('VIDEO COPY ID', 20) || RPAD('FORMAT', 10) || RPAD('STATUS', 10) || RPAD('CHECKOUT DATE', 20) || 'DUE DATE');
  DBMS_OUTPUT.PUT_LINE(LPAD('=', 58, '-'));
  FOR idx_1 IN c_video LOOP
    DBMS_OUTPUT.PUT_LINE(RPAD(idx_1.video_name, 20) || RPAD(idx_1.video_copy_id, 20) || RPAD(idx_1.format, 20) || RPAD(idx_1.status, 20) || RPAD(idx_1.checkout_date, 20) || RPAD(idx_1.due_date, 20));
  END LOOP;

  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('My exception: ' ||
        TO_CHAR(SQLCODE) || '   ' || SQLERRM);
END;

 PROCEDURE check_user_login
(
  in_user_id   NUMBER,
  in_password  VARCHAR2,
  out_code     OUT NUMBER
)
AS
  v_count NUMBER;
BEGIN
  SELECT COUNT(*)
    INTO v_count
    FROM dpu_user
   WHERE user_id = in_user_id;
  IF v_count = 0 THEN
    out_code := -1;
    RETURN;
  END IF;

  SELECT COUNT(*)
    INTO v_count
    FROM dpu_user
   WHERE user_id = in_user_id AND UPPER(is_locked_out) = 'Y';
  IF v_count = 1 THEN
    out_code := -2;
    RETURN;
  END IF;

  SELECT COUNT(*)
    INTO v_count
    FROM dpu_user
   WHERE user_id = in_user_id AND password = in_password;
  IF v_count = 1 THEN
    UPDATE dpu_user
    SET last_successful_login_time = SYSDATE,
        failed_password_attempt_count = 0,
        last_failed_login_time = NULL
    WHERE user_id = in_user_id;
    out_code := 1;
    RETURN;
  ELSE
    UPDATE dpu_user
    SET failed_password_attempt_count = failed_password_attempt_count + 1,
        last_failed_login_time = SYSDATE
    WHERE user_id = in_user_id;
    SELECT failed_password_attempt_count
      INTO v_count
      FROM dpu_user
     WHERE user_id = in_user_id;
    IF v_count != 5 THEN
      out_code := -3;
    ELSE
      UPDATE dpu_user
      SET is_locked_out = 'Y',
          last_locked_out_time = SYSDATE
      WHERE user_id = in_user_id;
      out_code := -4;
    END IF;
    RETURN;
  END IF;
END check_user_login;

3)
create or replace PROCEDURE reset_user_password
(
  in_user_id           NUMBER,
  in_current_password  VARCHAR2,
  in_new_password      VARCHAR2,
  out_code             OUT NUMBER
)
AS
  v_n NUMBER;
BEGIN
  SELECT COUNT(*)
    INTO v_n
    FROM dpu_user
   WHERE user_id = in_user_id;
  IF v_n = 0 THEN
    out_code := -1;
    RETURN;
  END IF;

  SELECT COUNT(*)
    INTO v_n
    FROM dpu_user
   WHERE user_id = in_user_id AND password = in_current_password;
  IF v_n = 0 THEN
    out_code := -2;
    RETURN;
  END IF;

  IF in_current_password = in_new_password THEN
    out_code := -3;
    RETURN;
  END IF;

  v_n := LENGTH(in_new_password);
  IF v_n < 10 OR v_n > 20 THEN
    out_code := -4;
    RETURN;
  END IF;

  SELECT COUNT(*)
    INTO v_n
    FROM DUAL
   WHERE REGEXP_LIKE(in_new_password, '[A-Z]');
  IF v_n = 0 THEN
    out_code := -5;
    RETURN;
  END IF;

  SELECT COUNT(*)
    INTO v_n
    FROM DUAL
  WHERE REGEXP_LIKE(in_new_password, '[a-z]');
  IF v_n = 0 THEN
    out_code := -6;
    RETURN;
  END IF;

  SELECT COUNT(*)
    INTO v_n
    FROM DUAL
   WHERE REGEXP_LIKE(in_new_password, '[0-9]');
  IF v_n = 0 THEN
    out_code := -7;
    RETURN;
  END IF;

  UPDATE dpu_user
     SET password = in_new_password,
         last_password_changed_time = SYSDATE
   WHERE user_id = in_user_id;
  out_code := 1;
  RETURN;
END;
