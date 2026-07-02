-- Migration: Create UUID v7 function
-- Description: Creates uuid_generate_v7() function for time-based UUID generation
--
-- UUID v7: time-based UUID that sorts by insertion time
-- Structure: 48-bit timestamp + 4-bit version + 2-bit variant + 74-bit random
-- Benefits: Optimal B-Tree performance (newer entries append sequentially)

CREATE OR REPLACE FUNCTION uuid_generate_v7()
RETURNS uuid AS $$
DECLARE
  timestamp    timestamptz := clock_timestamp();
  microseconds bigint;
BEGIN
  microseconds := (EXTRACT(epoch FROM timestamp) * 1000000)::bigint;
  RETURN encode(
    set_bit(
      set_bit(
        overlay(uuid_send(gen_random_uuid()) placing substring(decode(lpad(to_hex(microseconds / 1000), 12, '0'), 'hex') from 1 for 6) from 1 for 6),
        52, 1
      ), 53, 1
    ),
    'hex')::uuid;
END;
$$ LANGUAGE plpgsql VOLATILE;
