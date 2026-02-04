ALTER TABLE check_result
  DROP CONSTRAINT IF EXISTS check_result_target_id_fkey;

ALTER TABLE check_result
  ADD CONSTRAINT check_result_target_id_fkey
  FOREIGN KEY (target_id) REFERENCES target(id)
  ON DELETE CASCADE;
