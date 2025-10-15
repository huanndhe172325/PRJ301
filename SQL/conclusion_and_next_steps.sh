#!/bin/bash

# -----------------------------
# CMS Database Verification Script
# -----------------------------

echo "🔍 Verifying MySQL CMS database..."

# MySQL credentials (edit as needed)
MYSQL_USER="root"
MYSQL_PASSWORD="yourpassword"
MYSQL_DB="cms"

# Run MySQL queries
mysql -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DB" <<EOF
SELECT '--- Doctors ---';
SELECT * FROM doctor LIMIT 5;

SELECT '--- Doctor Availability ---';
SELECT * FROM doctor_available_times LIMIT 5;

SELECT '--- Patients ---';
SELECT * FROM patient LIMIT 5;

SELECT '--- Appointments ---';
SELECT * FROM appointment ORDER BY appointment_time LIMIT 5;

SELECT '--- Admins ---';
SELECT * FROM admin;
EOF

echo ""
echo "🧪 Verifying MongoDB prescriptions collection..."

# MongoDB verification
mongo <<EOF
use prescriptions;
db.prescriptions.find().limit(5).pretty();
EOF

echo ""
echo "✅ Verification complete. If anything looks off, please revisit your INSERT steps."
