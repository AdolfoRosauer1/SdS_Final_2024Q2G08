package itba.edu.ar.simulation;

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Copy constructor
    public Vector2D(Vector2D other) {
        this.x = other.x;
        this.y = other.y;
    }

    // Vector addition
    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    // Vector subtraction
    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    // Scalar multiplication
    public Vector2D multiply(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    // Scalar division
    public Vector2D divide(double scalar) {
        if (Math.abs(scalar) < 1e-10) {
            throw new IllegalArgumentException("Division by zero or near-zero");
        }
        return new Vector2D(this.x / scalar, this.y / scalar);
    }

    // Dot product
    public double dot(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    // Cross product (returns magnitude of the resulting vector in 3D)
    public double cross(Vector2D other) {
        return this.x * other.y - this.y * other.x;
    }

    // Vector magnitude (length)
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    // Square of magnitude (useful for comparisons without sqrt)
    public double magnitudeSquared() {
        return x * x + y * y;
    }

    // Normalize vector (create unit vector)
    public Vector2D normalize() {
        double mag = magnitude();
        if (mag < 1e-10) {
            return new Vector2D(0, 0);
        }
        return this.divide(mag);
    }

    // Rotate vector by angle (in radians)
    public Vector2D rotate(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector2D(
            x * cos - y * sin,
            x * sin + y * cos
        );
    }

    // Get angle between this vector and another
    public double angleBetween(Vector2D other) {
        double dot = this.dot(other);
        double mags = this.magnitude() * other.magnitude();
        if (mags < 1e-10) return 0.0;
        return Math.acos(Math.min(1.0, Math.max(-1.0, dot / mags)));
    }

    // Get angle from positive x-axis (in radians)
    public double angle() {
        return Math.atan2(y, x);
    }

    // Project this vector onto another vector
    public Vector2D projectOnto(Vector2D other) {
        double dot = this.dot(other);
        double magSquared = other.magnitudeSquared();
        if (magSquared < 1e-10) return new Vector2D(0, 0);
        return other.multiply(dot / magSquared);
    }

    // Get perpendicular vector (90 degrees counterclockwise)
    public Vector2D perpendicular() {
        return new Vector2D(-y, x);
    }

    // Distance to another vector
    public double distanceTo(Vector2D other) {
        return this.subtract(other).magnitude();
    }

    // Linear interpolation between this vector and another
    public Vector2D lerp(Vector2D other, double t) {
        t = Math.max(0.0, Math.min(1.0, t));
        return new Vector2D(
            this.x + (other.x - this.x) * t,
            this.y + (other.y - this.y) * t
        );
    }

    // Standard getters and setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // Override Object methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector2D)) return false;
        Vector2D other = (Vector2D) obj;
        return Math.abs(this.x - other.x) < 1e-10 && 
               Math.abs(this.y - other.y) < 1e-10;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) * 31 + Double.hashCode(y);
    }

    @Override
    public String toString() {
        return String.format("Vector2D(%.3f, %.3f)", x, y);
    }

    // Static utility methods
    public static Vector2D zero() {
        return new Vector2D(0, 0);
    }

    public static Vector2D unitX() {
        return new Vector2D(1, 0);
    }

    public static Vector2D unitY() {
        return new Vector2D(0, 1);
    }

    public static Vector2D fromAngle(double angle) {
        return new Vector2D(Math.cos(angle), Math.sin(angle));
    }

    public static Vector2D fromPolar(double magnitude, double angle) {
        return fromAngle(angle).multiply(magnitude);
    }
}
